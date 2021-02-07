package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.LogListener
import com.pandulapeter.beagle.commonBase.model.LogEntry

internal class LogListenerManager : BaseListenerManager<LogListener>() {

    fun notifyListeners(logEntry: LogEntry) = notifyListeners { it.onLogEntryAdded(logEntry) }
}