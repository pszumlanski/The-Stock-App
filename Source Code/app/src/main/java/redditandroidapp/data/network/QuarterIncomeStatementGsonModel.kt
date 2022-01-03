package redditandroidapp.data.network

import com.google.gson.annotations.SerializedName

// ApiResponse object used for deserializing data coming from API endpoint
data class QuarterIncomeStatementGsonModel(

    @SerializedName("grossProfit")
    val grossProfit: Double,

    @SerializedName("netIncome")
    val netIncome: Double,

    @SerializedName("reportedCurrency")
    val reportedCurrency: String,

    @SerializedName("eps")
    val eps: Double
)