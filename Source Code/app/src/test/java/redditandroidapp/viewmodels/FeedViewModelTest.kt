package redditandroidapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.repositories.CompaniesRepository
import redditandroidapp.features.feed.FeedViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class FeedViewModelTest {

    private var viewModel: FeedViewModel? = null
    private var fakeCompanyDatabaseEntity: CompanyDatabaseEntity? = null
    private var fakePostEntitiesList = ArrayList<CompanyDatabaseEntity>()

    @Mock
    private val companiesRepository: CompaniesRepository? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {

        // Inject Mocks
        MockitoAnnotations.initMocks(this)

        // Initialize the ViewModel
        viewModel = FeedViewModel(companiesRepository!!)

        // Prepare fake data
        val id = 0
        val title = "fake/post/title"
        val url = "fake/post/url"
        val imageUrl = "fake/post/image/url"
        val author = "fake/post/author"

        // Prepare fake Database Entity
        fakeCompanyDatabaseEntity = CompanyDatabaseEntity(id, url, title, imageUrl, author, null)

        // Prepare fake Database Entities List
        fakePostEntitiesList.add(fakeCompanyDatabaseEntity!!)
    }

    @Test
    fun fetchAllPostsByFeedViewModel() {

        // Prepare LiveData structure
        val postsEntityLiveData = MutableLiveData<List<CompanyDatabaseEntity>>()
        postsEntityLiveData.setValue(fakePostEntitiesList)

        // Set testing conditions
        Mockito.`when`(companiesRepository?.getAllPosts(false)).thenReturn(postsEntityLiveData)

        // Perform the action
        val storedPosts = viewModel?.subscribeForPosts(false)

        // Check results
        Assert.assertSame(postsEntityLiveData, storedPosts);
    }
}