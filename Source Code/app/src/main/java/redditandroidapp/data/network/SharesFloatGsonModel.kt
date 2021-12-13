package redditandroidapp.data.network

import com.google.gson.annotations.SerializedName

// ApiResponse object used for deserializing data coming from API endpoint
data class SharesFloatGsonModel(

    @SerializedName("outstandingShares")
    val outstandingShares: Long?
)