package redditandroidapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import javax.inject.Inject

// Interactor used for communication with the external API
class CompaniesNetworkInteractor @Inject constructor(var apiClient: ApiClient) {

    private val PERIOD_QUARTER = "quarter";
    private val PERIOD_ANNUAL = "annual";

    private val updateError: MutableLiveData<Boolean> = MutableLiveData()

    fun getIncomeStatementData(ticker: String): Call<List<QuarterIncomeStatementGsonModel>> {
        return apiClient.getIncomeStatementData(
                ticker,
                PERIOD_ANNUAL,
                2,
                apiKey = NetworkConstants.API_KEY
        )
    }

    fun getFloatSharesData(ticker: String): Call<List<SharesFloatGsonModel>> {
        return apiClient.getFloatSharesData(
                ticker,
                apiKey = NetworkConstants.API_KEY
        )
    }

    fun getSharePriceData(ticker: String): Call<List<SharePriceGsonModel>> {
        return apiClient.getSharePriceData(
                ticker,
                apiKey = NetworkConstants.API_KEY
        )
    }

    fun getUpdateError(): LiveData<Boolean>? {
        return updateError
    }

    fun setUpdateError(t: Throwable?) {
        updateError.postValue(true)
        if (t != null) { Log.e("Network Error: ", t.message) }
    }
}