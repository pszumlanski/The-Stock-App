package redditandroidapp.interactors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import redditandroidapp.data.database.CompaniesDao
import redditandroidapp.data.database.CompaniesDatabase
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.database.CompaniesDatabaseInteractor
import redditandroidapp.data.network.PostsResponseGsonModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import redditandroidapp.data.network.ChildrenPostsDataGsonModel
import redditandroidapp.data.network.SinglePostDataGsonModel

class CompaniesDatabaseInteractorTest {

    private var companiesDatabaseInteractor: CompaniesDatabaseInteractor? = null
    private var fakePostGsonModel: PostGsonModel? = null
    private var fakePostsResponseGsonModel: PostsResponseGsonModel? = null
    private var fakeCompanyDatabaseEntity: CompanyDatabaseEntity? = null

    @Mock
    private val companiesDatabase: CompaniesDatabase? = null

    @Mock
    private val companiesDao: CompaniesDao? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {

        // Inject Mocks
        MockitoAnnotations.initMocks(this)

        // Initialize the Interactor
        companiesDatabaseInteractor = CompaniesDatabaseInteractor(companiesDatabase!!)

        // Prepare fake data
        val id = 0
        val title = "fake/post/title"
        val url = "fake/post/url"
        val imageUrl = "fake/post/image/url"
        val author = "fake/post/author"

        // Prepare fake Gson (API) model objects
        fakePostGsonModel = PostGsonModel(url, title, imageUrl, author, null)
        fakePostsResponseGsonModel = PostsResponseGsonModel(
            ChildrenPostsDataGsonModel(
                listOf(SinglePostDataGsonModel(fakePostGsonModel!!))
            )
        )

        // Prepare fake Post Entity (DB object)
        fakeCompanyDatabaseEntity = CompanyDatabaseEntity(id, url, title, imageUrl, author, null)
    }

    @Test
    fun fetchSinglePostByDatabaseInteractor() {

        // Prepare LiveData structure
        val postEntityLiveData = MutableLiveData<CompanyDatabaseEntity>()
        postEntityLiveData.setValue(fakeCompanyDatabaseEntity);

        // Set testing conditions
        Mockito.`when`(companiesDatabase?.getPostsDao()).thenReturn(companiesDao)
        Mockito.`when`(companiesDao?.getSingleSavedPostById(anyInt())).thenReturn(postEntityLiveData)

        // Perform the action
        val storedPost = companiesDatabaseInteractor?.getSingleSavedPostById(0)

        // Check results
        Assert.assertSame(postEntityLiveData, storedPost);
    }

    @Test
    fun fetchAllPostsByDatabaseInteractor() {

        // Prepare LiveData structure
        val postEntityLiveData = MutableLiveData<List<CompanyDatabaseEntity>>()
        postEntityLiveData.setValue(listOf(fakeCompanyDatabaseEntity!!))

        // Set testing conditions
        Mockito.`when`(companiesDatabase?.getPostsDao()).thenReturn(companiesDao)
        Mockito.`when`(companiesDao?.getAllSavedPosts()).thenReturn(postEntityLiveData)

        // Perform the action
        val storedPost = companiesDatabaseInteractor?.getAllPosts()

        // Check results
        Assert.assertSame(postEntityLiveData, storedPost);
    }
}