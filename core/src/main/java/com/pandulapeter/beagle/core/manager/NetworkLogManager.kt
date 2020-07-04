package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.util.NetworkLogEntry

internal class NetworkLogManager {

    private val entries = mutableListOf<NetworkLogEntry>()

    fun log(entry: NetworkLogEntry) = entries.add(0, entry)

    fun clearLogs() = entries.clear()

    fun getEntries(): List<NetworkLogEntry> = entries
}