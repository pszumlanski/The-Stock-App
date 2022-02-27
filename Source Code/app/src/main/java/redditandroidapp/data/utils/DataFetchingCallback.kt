package redditandroidapp.data.utils

interface DataFetchingCallback {
    fun fetchingError(ticker: String, errorMessage: String?)
}