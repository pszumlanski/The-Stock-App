package redditandroidapp.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// External gate for communication with API endpoints (operated by Retrofit)
interface ApiClient {

    @GET("/api/v3/income-statement/{ticker}")
    fun getIncomeStatementData(
            @Path(value = "ticker") ticker: String,
            @Query("period") period: String,
            @Query("limit") amountOfQuarters: Int,
            @Query("apikey") apiKey: String
    ): Call<List<QuarterIncomeStatementGsonModel>>

    @GET("/api/v4/shares_float")
    fun getFloatSharesData(
            @Query(value = "symbol") ticker: String,
            @Query("apikey") apiKey: String
    ): Call<List<SharesFloatGsonModel>>

    @GET("/api/v3/quote-short/{ticker}")
    fun getSharePriceData(
            @Path(value = "ticker") ticker: String,
            @Query("apikey") apiKey: String
    ): Call<List<SharePriceGsonModel>>

}