package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.util.LogEntry

internal class LogManager {

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
    }

    fun clearLogs(tag: String?) {
        if (tag == null) {
            entries.clear()
        } else {
            entries.removeAll { it.tag == tag }
        }
    }

    fun getEntries(tag: String?): List<LogEntry> = if (tag == null) {
        entries
    } else {
        entries.filter { it.tag == tag }
    }
}