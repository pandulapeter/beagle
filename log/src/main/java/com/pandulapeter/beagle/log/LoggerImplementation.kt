package com.pandulapeter.beagle.log

import com.pandulapeter.beagle.commonBase.BeagleLoggerContract
import com.pandulapeter.beagle.commonBase.model.LogEntry

internal class LoggerImplementation : BeagleLoggerContract {

    private var onNewLog: ((LogEntry) -> Unit)? = null
    private var clearLogs: ((tag: String?) -> Unit)? = null

    override fun log(
        message: String,
        label: String?,
        payload: String?,
        isPersisted: Boolean,
        timestamp: Long,
        id: String
    ) {
        onNewLog?.invoke(
            LogEntry(
                id = id,
                label = label,
                message = message,
                payload = payload,
                isPersisted = isPersisted,
                timestamp = timestamp
            )
        )
    }

    override fun clearLogs(label: String?) {
        clearLogs?.invoke(label)
    }

    override fun register(
        onNewLog: (LogEntry) -> Unit,
        clearLogs: (label: String?) -> Unit
    ) {
        this.onNewLog = onNewLog
        this.clearLogs = clearLogs
    }
}