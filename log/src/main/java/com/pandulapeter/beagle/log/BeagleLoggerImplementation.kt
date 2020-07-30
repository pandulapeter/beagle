package com.pandulapeter.beagle.log

import com.pandulapeter.beagle.commonBase.BeagleLoggerContract

class BeagleLoggerImplementation : BeagleLoggerContract {

    private var onNewLog: ((message: CharSequence, tag: String?, payload: CharSequence?) -> Unit)? = null

    override fun register(onNewLog: (message: CharSequence, tag: String?, payload: CharSequence?) -> Unit) {
        this.onNewLog = onNewLog
    }

    override fun log(message: CharSequence, tag: String?, payload: CharSequence?) {
        onNewLog?.invoke(message, tag, payload)
    }
}