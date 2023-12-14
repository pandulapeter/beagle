package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UserWrapper(
    @Json(name = "users") val users: List<User>,
    @Json(name = "total") val total: Int
)