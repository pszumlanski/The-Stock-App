package redditandroidapp.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import redditandroidapp.data.database.CompaniesDatabaseInteractor
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.network.CompaniesNetworkInteractor
import redditandroidapp.data.repositories.CompaniesRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CompaniesRepositoryTest {

    private var companiesRepository: CompaniesRepository? = null
    private var fakeCompanyDatabaseEntity: CompanyDatabaseEntity? = null
    private var fakePostEntitiesList = ArrayList<CompanyDatabaseEntity>()

    @Mock
    private val companiesDatabaseInteractor: CompaniesDatabaseInteractor? = null

    @Mock
    private val companiesNetworkInteractor: CompaniesNetworkInteractor? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {

        // Inject Mocks
        MockitoAnnotations.initMocks(this)

        // Initialize the repository
        companiesRepository = CompaniesRepository(companiesNetworkInteractor!!, companiesDatabaseInteractor!!)

        // Prepare fake data
        val id = 0
        val title = "fake/post/title"
        val url = "fake/post/url"
        val imageUrl = "fake/post/image/url"
        val author = "fake/post/author"

        // Prepare fake Post Entity (DB object)
        fakeCompanyDatabaseEntity = CompanyDatabaseEntity(id, url, title, imageUrl, author, null)

        // Prepare fake Posts Entities List
        fakePostEntitiesList.add(fakeCompanyDatabaseEntity!!)
    }

    @Test
    fun fetchAllPostsByPostsRepository() {

        // Prepare LiveData structure
        val postsEntityLiveData = MutableLiveData<List<CompanyDatabaseEntity>>()
        postsEntityLiveData.setValue(fakePostEntitiesList);

        // Set testing conditions
        Mockito.`when`(companiesDatabaseInteractor?.getAllPosts()).thenReturn(postsEntityLiveData)

        // Perform the action
        val storedPosts = companiesRepository?.getAllPosts(false)

        // Check results
        Assert.assertSame(postsEntityLiveData, storedPosts);
    }

    @Test
    fun fetchSinglePostByPostsRepository() {

        // Prepare LiveData structure
        val postsEntityLiveData = MutableLiveData<CompanyDatabaseEntity>()
        postsEntityLiveData.setValue(fakeCompanyDatabaseEntity);

        // Prepare fake post id
        val fakePostId = 0

        // Set testing conditions
        Mockito.`when`(companiesDatabaseInteractor?.getSingleSavedPostById(fakePostId))
            .thenReturn(postsEntityLiveData)

        // Perform the action
        val storedPost = companiesRepository?.getSingleSavedPostById(fakePostId)

        // Check results
        Assert.assertSame(postsEntityLiveData, storedPost);
    }
}