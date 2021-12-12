package redditandroidapp.interactors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import redditandroidapp.data.network.*
import retrofit2.mock.Calls

class CompaniesNetworkInteractorTest {

    private var companiesNetworkInteractor: CompaniesNetworkInteractor? = null
    private var fakePostsResponseGsonModel: PostsResponseGsonModel? = null

    @Mock
    private val apiClient: ApiClient? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {

        // Inject Mocks
        MockitoAnnotations.initMocks(this)

        // Initialize the Interactor
        companiesNetworkInteractor = CompaniesNetworkInteractor(apiClient!!)

        // Prepare fake data
        val title = "fake/post/title"
        val url = "fake/post/url"
        val imageUrl = "fake/post/image/url"
        val author = "fake/post/author"

        // Prepare fake Gson (API) model objects
        val fakePostGsonModel = PostGsonModel(url, title, imageUrl, author, null)
        fakePostsResponseGsonModel = PostsResponseGsonModel(
            ChildrenPostsDataGsonModel(
                listOf(SinglePostDataGsonModel(fakePostGsonModel!!))
            )
        )
    }

    @Test
    fun fetchAllPostsByNetworkInteractor() {

        // Prepare API response
        val getAllPostsResponse = Calls.response(fakePostsResponseGsonModel!!)

        // Set testing conditions
        Mockito.`when`(apiClient?.getFreshPosts()).thenReturn(getAllPostsResponse)

        // Perform the action
        val storedPosts = companiesNetworkInteractor?.getFreshPosts()

        // Check results
        Assert.assertSame(getAllPostsResponse, storedPosts);

    }
}
