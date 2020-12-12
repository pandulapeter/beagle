package com.pandulapeter.beagle.common.listeners

/**
 * Implement this interface to get notified about when a new log message is added using Beagle.log().
 */
interface LogListener {

    /**
     * Called when the new log message is added.
     *
     * @param tag - Tag string that can be used to create filtered LogListModule instances.
     * @param message - The main content of the log entry.
     * @param payload - Optional, extra content.
     */
    fun onLogMessageAdded(tag: String?, message: CharSequence, payload: CharSequence?)
}