package redditandroidapp.injection

import dagger.Component
import redditandroidapp.data.database.CompaniesDatabaseInteractor
import redditandroidapp.data.network.CompaniesNetworkInteractor
import redditandroidapp.features.feed.FeedActivity
import redditandroidapp.features.feed.FeedViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, FeedModule::class, ViewModelModule::class))
interface MainComponent {
    fun inject(feedActivity: FeedActivity)
    fun inject(feedViewModel: FeedViewModel)
    fun inject(companiesNetworkInteractor: CompaniesNetworkInteractor)
    fun inject(companiesDatabaseInteractor: CompaniesDatabaseInteractor)
}