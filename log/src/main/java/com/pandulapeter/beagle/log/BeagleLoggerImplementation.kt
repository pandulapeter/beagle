package com.pandulapeter.beagle.log

import com.pandulapeter.beagle.commonBase.BeagleLoggerContract

class BeagleLoggerImplementation : BeagleLoggerContract {

    private var onNewLog: ((message: CharSequence, tag: String?, payload: CharSequence?) -> Unit)? = null
    private var clearLogs: ((tag: String?) -> Unit)? = null

    override fun log(message: CharSequence, tag: String?, payload: CharSequence?) {
        onNewLog?.invoke(message, tag, payload)
    }

    override fun clearLogs(tag: String?) {
        clearLogs?.invoke(tag)
    }

    override fun register(onNewLog: (message: CharSequence, tag: String?, payload: CharSequence?) -> Unit, clearLogs: (tag: String?) -> Unit) {
        this.onNewLog = onNewLog
        this.clearLogs = clearLogs
    }
}