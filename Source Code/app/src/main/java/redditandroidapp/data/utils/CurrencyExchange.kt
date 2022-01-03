package redditandroidapp.data.utils

import android.util.Log

class CurrencyExchange {

    companion object {

        val CURRENCY_USD = "USD"
        val CURRENCY_USD_RATE = 1.0

        val CURRENCY_JPY = "JPY"
        val CURRENCY_JPY_RATE = 0.0088

        val CURRENCY_CNY = "CNY"
        val CURRENCY_CNY_RATE = 0.16

        val CURRENCY_EUR = "EUR"
        val CURRENCY_EUR_RATE = 1.13

        val CURRENCY_GBP = "GBP"
        val CURRENCY_GBP_RATE = 1.32

        fun applyCurrencyExchange(currency: String?, multiplicand: Double): Double? {

            val multiplier = when (currency) {
                CURRENCY_USD -> CURRENCY_USD_RATE
                CURRENCY_JPY -> CURRENCY_JPY_RATE
                CURRENCY_CNY -> CURRENCY_CNY_RATE
                CURRENCY_EUR -> CURRENCY_EUR_RATE
                CURRENCY_GBP -> CURRENCY_GBP_RATE
                else -> null
            }

            return if (multiplier == null) null else multiplicand * multiplier
        }
    }
}