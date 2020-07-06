package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.manager.listener.LogListenerManager
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.modules.LogListModule

internal class LogManager(
    private val logListenerManager: LogListenerManager,
    private val listManager: ListManager,
    private val refresh: () -> Unit
) {
    private val entries = mutableListOf<LogEntry>()

    fun log(tag: String?, message: CharSequence, payload: CharSequence?) {
        entries.add(
            0,
            LogEntry(
                tag = tag,
                message = message,
                payload = payload
            )
        )
        entries.sortByDescending { it.timestamp }
        logListenerManager.notifyListeners(tag, message, payload)
        if (listManager.contains(LogListModule.formatId(null)) || listManager.contains(LogListModule.formatId(tag))) {
            refresh()
        }
    }

    fun clearLogs(tag: String?) {
        if (tag == null) {
            entries.clear()
        } else {
            entries.removeAll { it.tag == tag }
        }
        if (listManager.contains(LogListModule.formatId(null)) || listManager.contains(LogListModule.formatId(tag))) {
            refresh()
        }
    }

    fun getEntries(tag: String?): List<LogEntry> = if (tag == null) {
        entries
    } else {
        entries.filter { it.tag == tag }
    }
}