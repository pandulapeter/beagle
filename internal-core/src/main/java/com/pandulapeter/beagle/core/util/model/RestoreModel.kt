package com.pandulapeter.beagle.core.util.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestoreModel(
    @Json(name = "logs") val logs: List<SerializableLogEntry>,
    @Json(name = "networkLogs") val networkLogs: List<SerializableNetworkLogEntry>,
    @Json(name = "lifecycleLogs") val lifecycleLogs: List<SerializableLifecycleLogEntry>
)