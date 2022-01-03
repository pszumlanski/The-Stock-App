package redditandroidapp.data.database

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyDatabaseEntity(
        @PrimaryKey val ticker: String,

        // Fetched
        val previousQuarter_GrossProfit: Double?,
        val previousQuarter_NetIncome: Double?,
        val recentQuarter_GrossProfit: Double?,
        val recentQuarter_NetIncome: Double?,
        val eps: Double?,

        val today_OutstandingShares: Long?,
        val today_SharePrice: Double?
) {

    fun getGrossProfitChangeWithPreviousQuarter(): Double? {
        if (previousQuarter_GrossProfit == null || recentQuarter_GrossProfit == null) return null
        else {
            val result = (recentQuarter_GrossProfit - previousQuarter_GrossProfit)/previousQuarter_GrossProfit
            Log.d("CALCULATION CONTROL", ticker.toUpperCase() + ": "+ "getGrossProfitChangeWithPreviousQuarter: " + result)
            return result.toDouble()
        }
    }

    fun getNetIncomeChangeWithPreviousQuarter(): Double? {
        if (previousQuarter_NetIncome == null || recentQuarter_NetIncome == null) return null
        else {
            val result = (recentQuarter_NetIncome - previousQuarter_NetIncome)/previousQuarter_NetIncome
            Log.d("CALCULATION CONTROL", ticker.toUpperCase() + ": "+ "getNetIncomeChangeWithPreviousQuarter: " + result)
            return result.toDouble()
        }
    }

    fun getGrossProfitInRecentQuarterInCentPer1DollarSpentOnThemToday(): Double? {
        if (recentQuarter_GrossProfit == null || today_OutstandingShares == null || today_SharePrice == null) return null
        else {
            val result = (recentQuarter_GrossProfit/today_OutstandingShares/today_SharePrice * 100)
            Log.d("CALCULATION CONTROL", ticker.toUpperCase() + ": "+ "getGrossProfitInRecentQuarterInCentPer1DollarSpentOnThemToday: " + result)
            return result
        }
    }

    fun getNetIncomeInRecentQuarterInCentPer1DollarSpentOnThemToday(): Double? {
        if (recentQuarter_NetIncome == null || today_OutstandingShares == null || today_SharePrice == null) return null
        else {
            val result = (recentQuarter_NetIncome/today_OutstandingShares/today_SharePrice * 100)
            Log.d("CALCULATION CONTROL", ticker.toUpperCase() + ": "+ "getNetIncometInRecentQuarterInCentPer1DollarSpentOnThemToday: " + result)
            return result
        }
    }

    fun getEarningsPerShare(): Double? {
        return eps
    }
}

