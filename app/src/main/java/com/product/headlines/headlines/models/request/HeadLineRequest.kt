package com.product.headlines.headlines.models.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HeadLineRequest(
    @SerializedName("text") var pSearchText: String,
    @SerializedName("api_key") val apiKey: String,
    @SerializedName("method") val mMethod: String="flickr.photos.search",
    @SerializedName("format") val format: String="json",
    @SerializedName("nojsoncallback") var noOfCallbacks: String="1"
) : Serializable