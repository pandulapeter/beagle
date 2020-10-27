package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.manager.listener.LogListenerManager
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.modules.LogListModule

internal class LogManager(
    private val logListenerManager: LogListenerManager,
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    // TODO: Read all logs from storage
    private val entries = mutableListOf<LogEntry>()

    fun log(
        label: String?,
        message: CharSequence,
        payload: CharSequence?,
        isPersisted: Boolean,
        timestamp: Long,
        id: String
    ) {
        synchronized(entries) {
            entries.removeAll { it.id == id }
            entries.add(
                0,
                LogEntry(
                    id = id,
                    label = label,
                    message = message,
                    payload = payload,
                    timestamp = timestamp
                )
            )
            entries.sortByDescending { it.timestamp }
        }
        logListenerManager.notifyListeners(label, message, payload)
        refreshUiIfNeeded(label)
        if (isPersisted) {
            // TODO: Save log to storage
        }
    }

    fun clearLogs(label: String?) {
        synchronized(entries) {
            if (label == null) {
                entries.clear()
            } else {
                entries.removeAll { it.label == label }
            }
        }
        refreshUiIfNeeded(label)
        // TODO: Delete all logs from storage
    }

    fun getEntries(label: String?): List<LogEntry> = synchronized(entries) {
        if (label == null) {
            entries.toList()
        } else {
            entries.filter { it.label == label }.toList()
        }
    }

    private fun refreshUiIfNeeded(label: String?) {
        if (listManager.contains(LogListModule.formatId(null)) || listManager.contains(LogListModule.formatId(label))) {
            refreshUi()
        }
    }
}