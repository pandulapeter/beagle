package com.pandulapeter.beagle.common.listeners

import com.pandulapeter.beagle.commonBase.model.LogEntry

/**
 * Implement this interface to get notified about when a new log message is added using Beagle.log().
 */
interface LogListener {

    /**
     * Called when the new log entry is added.
     *
     * @param logEntry - The newly added [LogEntry].
     */
    fun onLogEntryAdded(logEntry: LogEntry)
}