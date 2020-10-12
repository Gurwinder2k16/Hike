package com.product.headlines.headlines.models.response

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class HeadlineResponse(
    @field:SerializedName("stat") val stat: String? = "",
    @field:SerializedName("photos") val photos: PhotoItems? = null
)

@Entity(tableName = "headline_news")
data class Photo(
    @field:SerializedName("owner") val owner: String? = "",
    @field:SerializedName("server") val server: String? = "",
    @field:SerializedName("ispublic") val ispublic: Int? = null,
    @field:SerializedName("isfriend") val isfriend: Int? = null,
    @field:SerializedName("farm") val farm: Int? = null,
    @NonNull @PrimaryKey @field:SerializedName("id") val id: String = "",
    @field:SerializedName("secret") val secret: String? = "",
    @field:SerializedName("title") val title: String? = "",
    @field:SerializedName("isfamily") val isfamily: Int? = null
)


data class PhotoItems(
    @field:SerializedName("perpage") val perpage: Int? = null,
    @field:SerializedName("total") val total: String? = "",
    @field:SerializedName("pages") val pages: Int=0,
    @field:SerializedName("photo") val photo: List<Photo> = ArrayList(),
    @field:SerializedName("page") val page: Int? = null
)
