package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.manager.listener.LogListenerManager
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.modules.LogListModule

internal class LogManager(
    private val logListenerManager: LogListenerManager,
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    private val entries = mutableListOf<LogEntry>()

    fun log(label: String?, message: CharSequence, payload: CharSequence?) {
        synchronized(entries) {
            entries.add(
                0,
                LogEntry(
                    label = label,
                    message = message,
                    payload = payload
                )
            )
            entries.sortByDescending { it.timestamp }
        }
        logListenerManager.notifyListeners(label, message, payload)
        refreshUiIfNeeded(label)
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