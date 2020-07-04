package com.pandulapeter.beagle.appDemo.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Song(
    @Json(name = "id") val id: String,
    @Json(name = "song") val text: String = ""
)