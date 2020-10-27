package com.pandulapeter.beagle.log

import com.pandulapeter.beagle.commonBase.BeagleLoggerContract

internal class LoggerImplementation : BeagleLoggerContract {

    private var onNewLog: ((
        message: String,
        tag: String?,
        payload: String?,
        isPersisted: Boolean,
        timestamp: Long,
        id: String
    ) -> Unit)? = null
    private var clearLogs: ((tag: String?) -> Unit)? = null

    override fun log(
        message: String,
        label: String?,
        payload: String?,
        isPersisted: Boolean,
        timestamp: Long,
        id: String
    ) {
        onNewLog?.invoke(message, label, payload, isPersisted, timestamp, id)
    }

    override fun clearLogs(label: String?) {
        clearLogs?.invoke(label)
    }

    override fun register(
        onNewLog: (
            message: String,
            label: String?,
            payload: String?,
            isPersisted: Boolean,
            timestamp: Long,
            id: String
        ) -> Unit,
        clearLogs: (label: String?) -> Unit
    ) {
        this.onNewLog = onNewLog
        this.clearLogs = clearLogs
    }
}