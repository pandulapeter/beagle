package com.pandulapeter.beagle.appDemo.data.model

import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@Serializable
@JsonClass(generateAdapter = true)
data class Song(
    @Json(name = Constants.KEY_SONG_ID)
//    @SerialName(Constants.KEY_SONG_ID)
    val id: String,
    @Json(name = Constants.KEY_SONG_TEXT)
//    @SerialName(Constants.KEY_SONG_TEXT)
    val text: String = ""
)