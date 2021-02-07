package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.manager.listener.NetworkLogListenerManager
import com.pandulapeter.beagle.core.util.model.SerializableNetworkLogEntry
import com.pandulapeter.beagle.modules.NetworkLogListModule

internal class NetworkLogManager(
    private val networkLogListenerManager: NetworkLogListenerManager,
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    private val entries = mutableListOf<SerializableNetworkLogEntry>()

    fun log(isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long, id: String) {
        val entry = SerializableNetworkLogEntry(
            id = id,
            isOutgoing = isOutgoing,
            payload = payload.orEmpty(),
            headers = headers.orEmpty(),
            url = url,
            duration = duration,
            timestamp = timestamp
        )
        synchronized(entries) {
            entries.removeAll { it.id == id }
            entries.add(0, entry)
            entries.sortByDescending { it.timestamp }
        }
        networkLogListenerManager.notifyListeners(entry.toNetworkLogEntry())
        if (listManager.contains(NetworkLogListModule.ID)) {
            refreshUi()
        }
    }

    fun restore(networkLogs: List<SerializableNetworkLogEntry>) {
        synchronized(entries) {
            entries.clear()
            entries.addAll(networkLogs.sortedByDescending { it.timestamp })
        }
        if (listManager.contains(NetworkLogListModule.ID)) {
            refreshUi()
        }
    }

    fun clearLogs() {
        synchronized(entries) { entries.clear() }
        if (listManager.contains(NetworkLogListModule.ID)) {
            refreshUi()
        }
    }

    /**
     * Returns the list of log entries created from Network Calls.
     *
     * New list creation is needed, otherwise we could not ensure there are no modifications while the list is being traversed via iterators.
     * Synchronization is needed to make the copy to ensure the are no additions or removals from the list while traversing it.
     */
    fun getEntries(): List<SerializableNetworkLogEntry> = synchronized(entries) { entries.toList() }
}