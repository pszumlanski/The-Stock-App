package redditandroidapp.data.network

import com.google.gson.annotations.SerializedName
import java.util.*

// ApiResponse object used for deserializing data coming from API endpoint
data class QuarterIncomeStatementGsonModel(

    @SerializedName("fillingDate")
    val fillingDate: String,

    @SerializedName("grossProfit")
    val grossProfit: Double,

    @SerializedName("netIncome")
    val netIncome: Double,

    @SerializedName("reportedCurrency")
    val reportedCurrency: String,

    @SerializedName("eps")
    val eps: Double
)