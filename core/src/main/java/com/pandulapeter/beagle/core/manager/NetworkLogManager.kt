package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.manager.listener.NetworkLogListenerManager
import com.pandulapeter.beagle.core.util.NetworkLogEntry
import com.pandulapeter.beagle.modules.NetworkLogListModule

internal class NetworkLogManager(
    private val networkLogListenerManager: NetworkLogListenerManager,
    private val listManager: ListManager,
    private val refresh: () -> Unit
) {
    private val entries = mutableListOf<NetworkLogEntry>()

    fun log(isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long) {
        val entry = NetworkLogEntry(
            isOutgoing = isOutgoing,
            payload = payload.orEmpty(),
            headers = headers.orEmpty(),
            url = url,
            duration = duration,
            timestamp = timestamp
        )
        entries.add(0, entry)
        entries.sortByDescending { it.timestamp }
        networkLogListenerManager.notifyListeners(entry)
        if (listManager.contains(NetworkLogListModule.ID)) {
            refresh()
        }
    }

    fun clearLogs() {
        entries.clear()
        if (listManager.contains(NetworkLogListModule.ID)) {
            refresh()
        }
    }

    fun getEntries(): List<NetworkLogEntry> = entries
}