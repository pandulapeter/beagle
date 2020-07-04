package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.util.NetworkLogEntry

internal class NetworkLogManager {

    private val entries = mutableListOf<NetworkLogEntry>()

    fun log(message: String, payload: String) = entries.add(
        0,
        NetworkLogEntry(
            message = message,
            payload = payload
        )
    )

    fun clearLogs() = entries.clear()

    fun getEntries(): List<NetworkLogEntry> = entries
}