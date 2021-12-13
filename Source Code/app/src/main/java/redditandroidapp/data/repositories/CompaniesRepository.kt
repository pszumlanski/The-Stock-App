package redditandroidapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import redditandroidapp.data.database.CompaniesDatabaseInteractor
import redditandroidapp.data.database.CompanyDatabaseEntity
import redditandroidapp.data.network.CompaniesNetworkInteractor
import redditandroidapp.data.network.QuarterIncomeStatementGsonModel
import redditandroidapp.data.network.SharePriceGsonModel
import redditandroidapp.data.network.SharesFloatGsonModel
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

    fun addNewCompany(ticker: String) {

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
                                    if (incomeStatementResponse.size == 2) {
                                        Log.d("DATA FETCHING", "Data fetched successfully")

                                        ///
                                        // Todo: It's temporary solution for next few days.
                                        // We need to implement fetching currency ratio from the API!
                                        val currency = incomeStatementResponse[0].reportedCurrency
                                        val multiplier = when (currency) {
                                            "USD" -> 1.0
                                            "JPY" -> 0.0088
                                            "CNY" -> 0.16
                                            "EUR" -> 1.13
                                            "GBP" -> 1.32
                                            else -> null
                                        }
                                        ///

                                        if (multiplier == null) {
                                            Log.d("CURRENCY CONTROL", "unknown currency: " + currency)
                                        } else {
                                            val previousQuarter_GrossProfit = incomeStatementResponse[1].grossProfit * multiplier
                                            val previousQuarter_NetIncome = incomeStatementResponse[1].netIncome * multiplier
                                            val recentQuarter_GrossProfit = incomeStatementResponse[0].grossProfit * multiplier
                                            val recentQuarter_NetIncome = incomeStatementResponse[0].netIncome * multiplier


                                            val today_OutstandingShares = floatSharesResponse[0].outstandingShares
                                            val today_SharePrice = sharePriceResponse[0].sharePrice

                                            Log.d("DATA CONTROL", "ticker: " + formattedTicker)
                                            Log.d("DATA CONTROL", "previousQuarter_GrossProfit: " + previousQuarter_GrossProfit.toString())
                                            Log.d("DATA CONTROL", "previousQuarter_NetIncome: " + previousQuarter_NetIncome.toString())
                                            Log.d("DATA CONTROL", "recentQuarter_GrossProfit: " + recentQuarter_GrossProfit.toString())
                                            Log.d("DATA CONTROL", "recentQuarter_NetIncome: " + recentQuarter_NetIncome.toString())
                                            Log.d("DATA CONTROL", "today_OutstandingShares: " + today_OutstandingShares.toString())
                                            Log.d("DATA CONTROL", "today_SharePrice: " + today_SharePrice.toString())

                                            val newCompany = CompanyDatabaseEntity(
                                                    ticker = ticker,

                                                    previousQuarter_GrossProfit = previousQuarter_GrossProfit,
                                                    previousQuarter_NetIncome = previousQuarter_NetIncome,

                                                    recentQuarter_GrossProfit = recentQuarter_GrossProfit,
                                                    recentQuarter_NetIncome = recentQuarter_NetIncome,

                                                    today_OutstandingShares = today_OutstandingShares,
                                                    today_SharePrice = today_SharePrice
                                            )
                                            databaseInteractor.addNewCompany(newCompany)
                                        }

                                    } else {
                                        // todo
                                    }
                                }
                                ////


                            }

                            override fun onFailure(call: Call<List<SharePriceGsonModel>>?, t: Throwable?) {
                                setUpdateError(t)
                                Log.e("DATA FETCHING", "Data fetching error - call 3")
                                t?.message?.let {
                                    Log.e("DATA FETCHING", ("Data fetching error - call 3 error: " + it))
                                }
                            }
                        })


                    }

                    override fun onFailure(call: Call<List<SharesFloatGsonModel>>?, t: Throwable?) {
                        setUpdateError(t)
                        Log.e("DATA FETCHING", "Data fetching error - call 2")
                        t?.message?.let {
                            Log.e("DATA FETCHING", ("Data fetching error - call 2 error: " + it))
                        }
                    }
                })


            }

            override fun onFailure(call: Call<List<QuarterIncomeStatementGsonModel>>?, t: Throwable?) {
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
