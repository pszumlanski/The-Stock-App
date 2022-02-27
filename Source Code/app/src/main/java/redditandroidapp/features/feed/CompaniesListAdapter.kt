package redditandroidapp.features.feed

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_feed_list_item.view.*
import redditandroidapp.R
import redditandroidapp.data.database.CompanyDatabaseEntity
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

// Main adapter used for managing items list within the main RecyclerView (main feed listed)
class CompaniesListAdapter(val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private var companiesList: List<CompanyDatabaseEntity> = ArrayList()

    // Todo: Refactor sortItems and setItems

    fun sortItems(sortingOption: SortingOption) {
        setItems(companiesList, sortingOption)
    }

    fun setItems(newCompaniesList: List<CompanyDatabaseEntity>, sortingOption: SortingOption) {

        when (sortingOption) {
            SortingOption.GROSS_PROFIT_CHANGE -> {
                this.companiesList = newCompaniesList.sortedByDescending { it.getGrossProfitChangeWithPreviousQuarter() }
            }
            SortingOption.NET_INCOME_CHANGE -> {
                this.companiesList = newCompaniesList.sortedByDescending { it.getNetIncomeChangeWithPreviousQuarter() }
            }
            SortingOption.GROSS_PROFIT_LAST_PERIOD -> {
                this.companiesList = newCompaniesList.sortedByDescending { it.getGrossProfitInRecentQuarterInCentPer1DollarSpentOnThemToday() }
            }
            SortingOption.NET_INCOME_LAST_PERIOD -> {
                this.companiesList = newCompaniesList.sortedByDescending { it.getNetIncomeInRecentQuarterInCentPer1DollarSpentOnThemToday() }
            }
            SortingOption.EPS_PER_1_DOLLAR_SPENT -> {
                this.companiesList = newCompaniesList.sortedByDescending { it.getEarningsPerSharePer1DollarSpentOnThemToday() }
            }
        }

        notifyDataSetChanged()
    }

    fun itemRemoved(position: Int) {
        notifyItemRemoved(position)
    }

    // Todo: Not the best solution - change!
    fun getCompanyIdByPosition(position: Int): String {
        return companiesList[position].ticker
    }

    override fun getItemCount(): Int {
        return companiesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.main_feed_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val company = companiesList[position]

        // Prepare fetched data
        val ticker = company.ticker.toUpperCase()
//
        // Set the picture
//        Glide.with(context)
//            .load(urlToImage)
//            .into(holder.picture)
        val rnd = Random()
        holder.picture.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))

        // Set data within the holder
        holder.ticker.text = ticker
        val decimalFormat = DecimalFormat("##.##%")

        company.getGrossProfitChangeWithPreviousQuarter()?.let {
            holder.grossProfitChange.text = (context.getString(R.string.gross_profit_change_with_previous) + "     " +
                    decimalFormat.format(it))
        }

        company.getNetIncomeChangeWithPreviousQuarter()?.let {
            holder.netIncomeChange.text = (context.getString(R.string.net_income_change_with_previous) + "     " +
                    decimalFormat.format(it))
        }

        company.getGrossProfitInRecentQuarterInCentPer1DollarSpentOnThemToday()?.let {
            holder.grossProfitPer1DollarSpent.text = (context.getString(R.string.gross_profit_in_recent_quarter) + "     " +
                    String.format("%.3f", it))
        }

        company.getNetIncomeInRecentQuarterInCentPer1DollarSpentOnThemToday()?.let {
            holder.netIncomePer1DollarSpent.text = (context.getString(R.string.net_income_in_recent_quarter) + "     " +
                    String.format("%.3f", it))
        }

        company.getEarningsPerShare()?.let {
            holder.eps.text = (context.getString(R.string.eps) + "     " + String.format("%.4f", it))
        }

        company.getEarningsPerSharePer1DollarSpentOnThemToday()?.let {
            holder.epsPer1DollarSpent.text = (context.getString(R.string.eps_per_dollar) + "     " + String.format("%.4f", it))
        }

        company.incomeStatementDate?.let {
            holder.incomeStatementDate.text = (context.getString(R.string.income_state_date) + "     " + it)
        }

//        // Set onClickListener
//        holder.rowContainer.setOnClickListener{
//            val itemId = postsList[position].id
//            clickListener(itemId)
//        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val picture = view.imageView_picture
    val ticker = view.textView_ticker

    val grossProfitChange = view.textView_grossProfitChange
    val netIncomeChange = view.textView_netIncomeChange

    val grossProfitPer1DollarSpent = view.textView_grossProfitPerSpentDollar
    val netIncomePer1DollarSpent = view.textView_netIncomePerSpentDollar

    val eps = view.textView_eps
    val epsPer1DollarSpent = view.textView_epsPerSpentDollar

    val incomeStatementDate = view.textView_incomeStatementDate

    val rowContainer = view.row_container
}

enum class SortingOption {
    GROSS_PROFIT_CHANGE,
    NET_INCOME_CHANGE,
    GROSS_PROFIT_LAST_PERIOD,
    NET_INCOME_LAST_PERIOD,
    EPS_PER_1_DOLLAR_SPENT
}