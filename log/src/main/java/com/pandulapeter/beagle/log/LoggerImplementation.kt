package com.pandulapeter.beagle.log

import com.pandulapeter.beagle.commonBase.BeagleLoggerContract

internal class LoggerImplementation : BeagleLoggerContract {

    private var onNewLog: ((message: CharSequence, tag: String?, payload: CharSequence?) -> Unit)? = null
    private var clearLogs: ((tag: String?) -> Unit)? = null

    override fun log(message: CharSequence, label: String?, payload: CharSequence?) {
        onNewLog?.invoke(message, label, payload)
    }

    override fun clearLogs(label: String?) {
        clearLogs?.invoke(label)
    }

    override fun register(onNewLog: (message: CharSequence, label: String?, payload: CharSequence?) -> Unit, clearLogs: (label: String?) -> Unit) {
        this.onNewLog = onNewLog
        this.clearLogs = clearLogs
    }
}