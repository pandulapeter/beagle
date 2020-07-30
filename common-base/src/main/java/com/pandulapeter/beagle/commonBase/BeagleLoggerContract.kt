package com.pandulapeter.beagle.commonBase

interface BeagleLoggerContract {

    /**
     * Adds a new log handled by instances of LogListModule and notifies the registered LogListeners.
     *
     * @param message - The message that will be displayed.
     * @param tag - Optional tag that can be used to create filtered LogListModule instances, null by default.
     * @param payload - Extra message that will only be displayed when the user selects the log entry. Entries with payloads are marked with "*" at the end. Optional, null by default.
     */
    fun log(message: CharSequence, tag: String? = null, payload: CharSequence? = null) = Unit

    /**
     * For internal use only.
     */
    fun register(onNewLog: (message: CharSequence, tag: String?, payload: CharSequence?) -> Unit) = Unit
}