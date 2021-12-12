package redditandroidapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.repositories.CompaniesRepository
import redditandroidapp.features.detailedview.DetailedViewViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailedViewViewModelTest {

    private var viewModel: DetailedViewViewModel? = null
    private var fakeCompanyDatabaseEntity: CompanyDatabaseEntity? = null

    @Mock
    private val companiesRepository: CompaniesRepository? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {

        // Inject Mocks
        MockitoAnnotations.initMocks(this)

        // Initialize the ViewModel
        viewModel = DetailedViewViewModel(companiesRepository!!)

        // Prepare fake data
        val id = 0
        val title = "fake/post/title"
        val url = "fake/post/url"
        val imageUrl = "fake/post/image/url"
        val author = "fake/post/author"

        // Prepare fake Database Entity
        fakeCompanyDatabaseEntity = CompanyDatabaseEntity(id, url, title, imageUrl, author, null)
    }

    @Test
    fun fetchSinglePostByDetailedViewViewModel() {

        // Prepare LiveData structure
        val postEntityLiveData = MutableLiveData<CompanyDatabaseEntity>()
        postEntityLiveData.setValue(fakeCompanyDatabaseEntity);

        // Prepare fake post id
        val fakePostId = 0

        // Set testing conditions
        Mockito.`when`(companiesRepository?.getSingleSavedPostById(fakePostId)).thenReturn(postEntityLiveData)

        // Perform the action
        val storedPost = viewModel?.getSingleSavedPostById(fakePostId)

        // Check results
        Assert.assertSame(postEntityLiveData, storedPost);
    }
}