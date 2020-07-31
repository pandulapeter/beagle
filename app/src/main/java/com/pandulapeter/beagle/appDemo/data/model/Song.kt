package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Song(
    @SerializedName(Constants.KEY_SONG_ID)
    @Json(name = Constants.KEY_SONG_ID)
    val id: String,
    @SerializedName(Constants.KEY_SONG_TEXT)
    @Json(name = Constants.KEY_SONG_TEXT)
    val text: String = ""
)