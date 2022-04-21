package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.Keep
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Song(
    @Json(name = Constants.KEY_SONG_ID)
    val id: String,
    @Json(name = Constants.KEY_SONG_TEXT)
    val text: String = ""
)