package redditandroidapp.features.feed

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.loading_badge.*
import redditandroidapp.R
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.injection.RedditAndroidApp
import javax.inject.Inject


// Main ('feed') view
class FeedActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FeedViewModel
    private lateinit var companiesListAdapter: CompaniesListAdapter
    var isLoadingMoreItems: Boolean = false

    private val STATE_LOADING_ERROR = "STATE_LOADING_ERROR"
    private val STATE_CONTENT_LOADED = "STATE_CONTENT_LOADED"

    init {
        RedditAndroidApp.mainComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel::class.java)

        // Initialize RecyclerView (feed items)
        setupRecyclerView()

        // Fetch feed items from the back-end and load them into the view
        subscribeForFeedItems()

        // Catch and handle potential update (e.g. network) issues
        subscribeForUpdateError()

        // Load the default companies set
        loadDefaultCompaniesSet()

        // TODO CLEAN
        add_company_button.setOnClickListener {
            val ticker = add_company_input.text.toString()
            viewModel.addCompany(ticker)

            // Hide the keyboard
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(add_company_input.getWindowToken(), 0)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        main_feed_recyclerview.layoutManager = layoutManager
        companiesListAdapter = CompaniesListAdapter(this)
        main_feed_recyclerview.adapter = companiesListAdapter


        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val ticker = companiesListAdapter.getCompanyIdByPosition(viewHolder.adapterPosition)
                viewModel.removeCompany(ticker)
                companiesListAdapter.itemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(main_feed_recyclerview)
    }

    private fun subscribeForFeedItems() {
        viewModel.subscribeForPosts()?.observe(this, Observer<List<CompanyDatabaseEntity>> {
            setViewState(STATE_CONTENT_LOADED)

            // Display fetched items
            companiesListAdapter.setItems(it)
        })
    }

    private fun loadDefaultCompaniesSet() {
        val defaultCompanies = arrayListOf<String>()

        defaultCompanies.add("GOOGL")
        defaultCompanies.add("HMC")
        defaultCompanies.add("NIO")
        defaultCompanies.add("SQ")
        defaultCompanies.add("TM")
        defaultCompanies.add("TSLA")
        defaultCompanies.add("XPEV")

        defaultCompanies.forEach {
            viewModel.addCompany(it)
        }
    }

    private fun subscribeForUpdateError() {
        viewModel.subscribeForUpdateErrors()?.observe(this, Observer<Boolean> {

            // Case of Network Error if no items have been cached
            if (companiesListAdapter.itemCount == 0) {
                setViewState(STATE_LOADING_ERROR)
            }

            // Display error message to the user
            Toast.makeText(this, R.string.network_problem_check_internet_connection, Toast.LENGTH_LONG).show()

            isLoadingMoreItems = false
        })
    }

    private fun refreshPostsSubscription() {
//        viewModel.refreshPosts()
    }

    private fun setViewState(state: String) {
        when(state) {
            STATE_LOADING_ERROR -> setupLoadingErrorView()
            STATE_CONTENT_LOADED -> setupContentLoadedView()
        }
    }

    private fun setupLoadingErrorView() {
        // Stop the loading progress bar (circle)
        progressBar.visibility = View.INVISIBLE

        // Display "Try Again" button
        tryagain_button.visibility = View.VISIBLE

        // Setup onClick listener that resets the feed data subscription
        tryagain_button.setOnClickListener {
            refreshPostsSubscription()

            // Re-display the loading progress bar (circle)
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun setupContentLoadedView() {
        // Hide the loading view
        loading_container.visibility = View.GONE
        appbar_container.visibility = View.VISIBLE

        // Setup refresh button
        btn_refresh.setOnClickListener{
            refreshPostsSubscription()
        }
    }
}
