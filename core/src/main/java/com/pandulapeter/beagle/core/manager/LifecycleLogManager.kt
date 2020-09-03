package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.modules.LifecycleLogListModule

internal class LifecycleLogManager(
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    private val entries = mutableListOf<LogEntry>()

    fun log(classType: Class<*>, lifecycleEvent: String) {
        synchronized(entries) {
            entries.add(
                0,
                LogEntry(
                    label = null,
                    message = "${classType.simpleName}: $lifecycleEvent",
                    payload = null
                )
            )
            entries.sortByDescending { it.timestamp }
        }
        if (listManager.contains(LifecycleLogListModule.ID)) {
            refreshUi()
        }
    }

    fun getEntries(): List<LogEntry> = synchronized(entries) {
        entries.toList()
    }
}