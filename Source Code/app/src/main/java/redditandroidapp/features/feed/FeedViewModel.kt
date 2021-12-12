package redditandroidapp.features.feed

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.repositories.CompaniesRepository
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val companiesRepository: CompaniesRepository)
    : ViewModel(), LifecycleObserver {

    fun subscribeForPosts(): LiveData<List<CompanyDatabaseEntity>>? {
        return companiesRepository.getAllCompanies()
    }

    fun addCompany(ticker: String) {
        companiesRepository.addNewCompany(ticker)
    }

    fun removeCompany(ticker: String) {
        companiesRepository.removeCompany(ticker)
    }

    fun subscribeForUpdateErrors(): LiveData<Boolean>? {
        return companiesRepository.subscribeForUpdateErrors()
    }
}