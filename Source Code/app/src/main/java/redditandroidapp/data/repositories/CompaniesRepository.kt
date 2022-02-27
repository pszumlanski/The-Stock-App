package redditandroidapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import redditandroidapp.data.database.CompaniesDatabaseInteractor
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.network.CompaniesNetworkInteractor
import redditandroidapp.data.network.QuarterIncomeStatementGsonModel
import redditandroidapp.data.network.SharePriceGsonModel
import redditandroidapp.data.network.SharesFloatGsonModel
import redditandroidapp.data.utils.CurrencyExchange
import redditandroidapp.data.utils.DataFetchingCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// Data Repository - the main gate of the model (data) part of the application
class CompaniesRepository @Inject constructor(private val networkInteractor: CompaniesNetworkInteractor,
                                              private val databaseInteractor: CompaniesDatabaseInteractor
) {

    fun getAllCompanies(): LiveData<List<CompanyDatabaseEntity>>? {
        return databaseInteractor.getAllCompanies()
    }

    fun subscribeForUpdateErrors(): LiveData<Boolean>? {
        return networkInteractor.getUpdateError()
    }

    fun setUpdateError(t: Throwable?) {
        networkInteractor.setUpdateError(t)
    }

    fun addNewCompany(ticker: String, callback: DataFetchingCallback) {

        val formattedTicker = ticker.toUpperCase()


        // First API call
        networkInteractor.getIncomeStatementData(formattedTicker).enqueue(object: Callback<List<QuarterIncomeStatementGsonModel>> {

            override fun onResponse(call: Call<List<QuarterIncomeStatementGsonModel>>?, response: Response<List<QuarterIncomeStatementGsonModel>>?) {

                val incomeStatementResponse = response?.body()


                // Second API call
                networkInteractor.getFloatSharesData(formattedTicker).enqueue(object: Callback<List<SharesFloatGsonModel>> {

                    override fun onResponse(call: Call<List<SharesFloatGsonModel>>?, response: Response<List<SharesFloatGsonModel>>?) {

                        val floatSharesResponse = response?.body()


                        // Third API call
                        networkInteractor.getSharePriceData(formattedTicker).enqueue(object: Callback<List<SharePriceGsonModel>> {

                            override fun onResponse(call: Call<List<SharePriceGsonModel>>?, response: Response<List<SharePriceGsonModel>>?) {

                                val sharePriceResponse = response?.body()

                                ////
                                if (incomeStatementResponse != null && floatSharesResponse != null && sharePriceResponse != null) {
                                    if (incomeStatementResponse.size == 2 && floatSharesResponse.isNotEmpty()) {
                                        Log.d("DATA FETCHING", "Data fetched successfully")

                                        val currency = incomeStatementResponse[0].reportedCurrency
                                        val fillingDate = incomeStatementResponse[0].fillingDate
                                        val previousQuarter_GrossProfit = CurrencyExchange.applyCurrencyExchange(currency ,incomeStatementResponse[1].grossProfit)
                                        val previousQuarter_NetIncome = CurrencyExchange.applyCurrencyExchange(currency ,incomeStatementResponse[1].netIncome)
                                        val recentQuarter_GrossProfit = CurrencyExchange.applyCurrencyExchange(currency ,incomeStatementResponse[0].grossProfit)
                                        val recentQuarter_NetIncome = CurrencyExchange.applyCurrencyExchange(currency ,incomeStatementResponse[0].netIncome)
                                        val eps = CurrencyExchange.applyCurrencyExchange(currency ,incomeStatementResponse[0].eps)

                                        val today_OutstandingShares = floatSharesResponse[0].outstandingShares
                                        val today_SharePrice = sharePriceResponse[0].sharePrice

                                        Log.d("DATA CONTROL", "ticker: " + formattedTicker)
                                        Log.d("DATA CONTROL", "fillingDate: " + fillingDate)
                                        Log.d("DATA CONTROL", "previousQuarter_GrossProfit: " + previousQuarter_GrossProfit.toString())
                                        Log.d("DATA CONTROL", "previousQuarter_NetIncome: " + previousQuarter_NetIncome.toString())
                                        Log.d("DATA CONTROL", "recentQuarter_GrossProfit: " + recentQuarter_GrossProfit.toString())
                                        Log.d("DATA CONTROL", "recentQuarter_NetIncome: " + recentQuarter_NetIncome.toString())
                                        Log.d("DATA CONTROL", "today_OutstandingShares: " + today_OutstandingShares.toString())
                                        Log.d("DATA CONTROL", "today_SharePrice: " + today_SharePrice.toString())

                                        val newCompany = CompanyDatabaseEntity(
                                                ticker = ticker,
                                                incomeStatementDate = fillingDate,

                                                previousQuarter_GrossProfit = previousQuarter_GrossProfit,
                                                previousQuarter_NetIncome = previousQuarter_NetIncome,

                                                recentQuarter_GrossProfit = recentQuarter_GrossProfit,
                                                recentQuarter_NetIncome = recentQuarter_NetIncome,

                                                eps = eps,

                                                today_OutstandingShares = today_OutstandingShares,
                                                today_SharePrice = today_SharePrice
                                        )
                                        databaseInteractor.addNewCompany(newCompany)
                                    } else {
                                        val message = "incomeStatementResponse size less than 2 or floatSharesResponse is empty"
                                        callback.fetchingError(ticker, message)
                                        setUpdateError(null)
                                        Log.e("DATA FETCHING", "Data fetching error - data incomplete")
                                    }
                                } else {
                                    val message = "lack of incomeStatementResponse or floatSharesResponse or sharePriceResponse"
                                    callback.fetchingError(ticker, message)
                                    setUpdateError(null)
                                    Log.e("DATA FETCHING", "Data fetching error - data incomplete")
                                }

                            }

                            override fun onFailure(call: Call<List<SharePriceGsonModel>>?, t: Throwable?) {
                                callback.fetchingError(ticker, t?.message)
                                setUpdateError(t)
                                Log.e("DATA FETCHING", "Data fetching error - call 3")
                                t?.message?.let {
                                    Log.e("DATA FETCHING", ("Data fetching error - call 3 error: " + it))
                                }
                            }
                        })


                    }

                    override fun onFailure(call: Call<List<SharesFloatGsonModel>>?, t: Throwable?) {
                        callback.fetchingError(ticker, t?.message)
                        setUpdateError(t)
                        Log.e("DATA FETCHING", "Data fetching error - call 2")
                        t?.message?.let {
                            Log.e("DATA FETCHING", ("Data fetching error - call 2 error: " + it))
                        }
                    }
                })


            }

            override fun onFailure(call: Call<List<QuarterIncomeStatementGsonModel>>?, t: Throwable?) {
                callback.fetchingError(ticker, t?.message)
                setUpdateError(t)
                Log.e("DATA FETCHING", "Data fetching error - call 1")
                t?.message?.let {
                    Log.e("DATA FETCHING", ("Data fetching error - call 1 error: " + it))
                }
            }
        })
    }

    fun removeCompany(ticker: String) {
        databaseInteractor.removeCompany(ticker)
    }
}
