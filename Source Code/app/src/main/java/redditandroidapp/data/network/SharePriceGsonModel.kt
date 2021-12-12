package redditandroidapp.data.network

import com.google.gson.annotations.SerializedName

// ApiResponse object used for deserializing data coming from API endpoint
data class SharePriceGsonModel(

    @SerializedName("price")
    val sharePrice: Double?
)