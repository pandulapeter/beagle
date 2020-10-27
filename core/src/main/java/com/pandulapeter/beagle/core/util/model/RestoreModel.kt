package com.pandulapeter.beagle.core.util.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class RestoreModel(
    @Json(name = "logs") val logs: List<LogEntry>,
    @Json(name = "networkLogs") val networkLogs: List<NetworkLogEntry>,
    @Json(name = "lifecycleLogs") val lifecycleLogs: List<LifecycleLogEntry>
)