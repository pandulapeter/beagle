package com.pandulapeter.beagle.core.manager

import android.app.Application
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.manager.listener.LogListenerManager
import com.pandulapeter.beagle.core.util.extension.LOG_PREFIX
import com.pandulapeter.beagle.core.util.extension.createPersistedLogFile
import com.pandulapeter.beagle.core.util.extension.getPersistedLogsFolder
import com.pandulapeter.beagle.core.util.extension.readLogEntryFromLogFile
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.modules.LogListModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class LogManager(
    private val logListenerManager: LogListenerManager,
    private val listManager: ListManager,
    private val refreshUi: () -> Unit
) {
    var application: Application? = null
        set(value) {
            field = value
            syncIfNeeded()
        }
    private var isSyncReady = false
    private val entries = mutableListOf<SerializableLogEntry>()
        get() {
            syncIfNeeded()
            return field
        }
    private val maximumEntryCount get() = BeagleCore.implementation.behavior.logBehavior.maximumLogCount

    fun log(
        label: String?,
        message: String,
        payload: String?,
        isPersisted: Boolean,
        timestamp: Long,
        id: String
    ) {
        val entry = SerializableLogEntry(
            id = id,
            label = label,
            message = message,
            payload = payload,
            isPersisted = isPersisted,
            timestamp = timestamp
        )
        synchronized(entries) {
            entries.removeAll { it.id == id }
            entries.add(0, entry)
            if (entries.size > maximumEntryCount) {
                entries.removeAt(entries.lastIndex)
            }
            entries.sortByDescending { it.timestamp }
        }
        logListenerManager.notifyListeners(entry.toLogEntry())
        refreshUiIfNeeded(label)
        if (isPersisted) {
            GlobalScope.launch(Dispatchers.IO) {
                application?.createPersistedLogFile(entry)
            }
        }
    }

    fun restore(logs: List<SerializableLogEntry>) {
        synchronized(entries) {
            entries.clear()
            entries.addAll(logs.take(maximumEntryCount).sortedByDescending { it.timestamp })
        }
        refreshUiIfNeeded(null)
        syncIfNeeded()
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
        GlobalScope.launch(Dispatchers.IO) {
            application?.getPersistedLogsFolder()?.listFiles()?.forEach { file ->
                if (file.name.startsWith(LOG_PREFIX) && readLogEntryFromLogFile(file)?.label == label) {
                    file.delete()
                }
            }
        }
    }

    fun getEntries(label: String?): List<SerializableLogEntry> = synchronized(entries) {
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

    private fun syncIfNeeded() {
        if (!isSyncReady) {
            application?.let { context ->
                GlobalScope.launch(Dispatchers.IO) {
                    val persistedEntries = context.getPersistedLogsFolder().listFiles()?.mapNotNull { file ->
                        if (file.name.startsWith(LOG_PREFIX)) readLogEntryFromLogFile(file) else null
                    }.orEmpty()
                    isSyncReady = true
                    if (persistedEntries.isNotEmpty()) {
                        synchronized(entries) {
                            val allEntries = (entries + persistedEntries).asSequence().distinctBy { it.id }.take(maximumEntryCount).sortedByDescending { it.timestamp }
                            entries.clear()
                            entries.addAll(allEntries)
                            if (persistedEntries.isNotEmpty()) {
                                refreshUiIfNeeded(null)
                            }
                        }
                    }
                }
            }
        }
    }
}