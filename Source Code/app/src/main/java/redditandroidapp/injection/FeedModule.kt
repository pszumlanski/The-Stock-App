package redditandroidapp.injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import redditandroidapp.data.database.CompaniesDatabase
import redditandroidapp.data.database.CompaniesDatabaseInteractor
import redditandroidapp.data.network.ApiClient
import redditandroidapp.data.network.CompaniesNetworkInteractor
import redditandroidapp.data.network.NetworkAdapter
import redditandroidapp.data.repositories.CompaniesRepository
import javax.inject.Singleton

@Module
class FeedModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Context): CompaniesDatabase {
        return Room.databaseBuilder(application, CompaniesDatabase::class.java, "main_database").build()
    }

    @Provides
    @Singleton
    fun providesCompaniesDatabaseInteractor(companiesDatabase: CompaniesDatabase): CompaniesDatabaseInteractor {
        return CompaniesDatabaseInteractor(companiesDatabase)
    }

    @Provides
    @Singleton
    fun providesCompaniesNetworkInteractor(apiClient: ApiClient): CompaniesNetworkInteractor {
        return CompaniesNetworkInteractor(apiClient)
    }

    @Provides
    @Singleton
    fun providesApiClient(): ApiClient {
        return NetworkAdapter.apiClient()
    }

    @Provides
    @Singleton
    fun providesCompaniesRepository(companiesNetworkInteractor: CompaniesNetworkInteractor,
                                companiesDatabaseInteractor: CompaniesDatabaseInteractor
    ): CompaniesRepository {
        return CompaniesRepository(companiesNetworkInteractor, companiesDatabaseInteractor)
    }
}