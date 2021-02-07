package com.pandulapeter.beagle.core.util.model

import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerializableNetworkLogEntry(
    @Json(name = "id") override val id: String,
    @Json(name = "isOutgoing") val isOutgoing: Boolean,
    @Json(name = "payload") val payload: String,
    @Json(name = "headers") val headers: List<String> = emptyList(),
    @Json(name = "url") val url: String,
    @Json(name = "duration") val duration: Long? = null,
    @Json(name = "timestamp") val timestamp: Long
) : BeagleListItemContract {

    override val title = url.toText()

    fun toNetworkLogEntry() = NetworkLogEntry(
        id = id,
        isOutgoing = isOutgoing,
        url = url,
        payload = payload,
        headers = headers,
        duration = duration,
        timestamp = timestamp
    )
}