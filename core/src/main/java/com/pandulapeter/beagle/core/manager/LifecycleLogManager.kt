package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.core.util.LifecycleLogEntry
import com.pandulapeter.beagle.modules.LifecycleLogListModule

internal class LifecycleLogManager(
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    private val entries = mutableListOf<LifecycleLogEntry>()

    fun log(classType: Class<*>, eventType: LifecycleLogListModule.EventType, hasSavedInstanceState: Boolean?) {
        synchronized(entries) {
            entries.add(0, LifecycleLogEntry(classType, eventType, hasSavedInstanceState))
            entries.sortByDescending { it.timestamp }
        }
        if (listManager.contains(LifecycleLogListModule.ID)) {
            refreshUi()
        }
    }

    fun getEntries(eventTypes: List<LifecycleLogListModule.EventType>?): List<LifecycleLogEntry> = synchronized(entries) {
        if (eventTypes == null) {
            entries.toList()
        } else {
            entries.filter { eventTypes.contains(it.eventType) }.toList()
        }
    }

    fun clearLogs() = synchronized(entries) {
        entries.clear()
    }
}