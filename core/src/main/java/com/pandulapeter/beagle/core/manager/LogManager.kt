package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.util.LogEntry

internal class LogManager {

    private val entries = mutableListOf<LogEntry>()

    fun log(tag: String?, message: String, payload: String?) = entries.add(
        0,
        LogEntry(
            tag = tag,
            message = message,
            payload = payload
        )
    )

    fun getEntries(tag: String?): List<LogEntry> = if (tag == null) {
        entries
    } else {
        entries.filter { it.tag == tag }
    }

}