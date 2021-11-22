package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.model.SerializableLifecycleLogEntry
import com.pandulapeter.beagle.modules.LifecycleLogListModule

internal class LifecycleLogManager(
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    private val entries = mutableListOf<SerializableLifecycleLogEntry>()
    private val maximumEntryCount get() = BeagleCore.implementation.behavior.lifecycleLogBehavior.maximumLogCount

    fun log(classType: Class<*>, eventType: LifecycleLogListModule.EventType, hasSavedInstanceState: Boolean?) {
        synchronized(entries) {
            entries.add(0, SerializableLifecycleLogEntry(classType, eventType, hasSavedInstanceState))
            if (entries.size > maximumEntryCount) {
                entries.removeAt(entries.lastIndex)
            }
            entries.sortByDescending { it.timestamp }
        }
        if (listManager.contains(LifecycleLogListModule.ID)) {
            refreshUi()
        }
    }

    fun restore(lifecycleLogs: List<SerializableLifecycleLogEntry>) {
        synchronized(entries) {
            entries.clear()
            entries.addAll(lifecycleLogs.take(maximumEntryCount).sortedByDescending { it.timestamp })
        }
        if (listManager.contains(LifecycleLogListModule.ID)) {
            refreshUi()
        }
    }

    fun getEntries(eventTypes: List<LifecycleLogListModule.EventType>?): List<SerializableLifecycleLogEntry> = synchronized(entries) {
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