package com.pandulapeter.beagle.commonBase

interface BeagleLoggerContract {

    /**
     * Adds a new log message handled by instances of LogListModule and notifies the registered LogListeners.
     *
     * @param message - The message that will be displayed.
     * @param label - Optional tag that can be used to create filtered LogListModule instances, null by default.
     * @param payload - Extra message that will only be displayed when the user selects the log entry. Entries with payloads are marked with "*" at the end. Optional, null by default.
     * @param isPersisted - If true, the log will be saved to local storage. If false, it will only be kept in memory as long as the app is running. False by default.
     * @param timestamp - The moment the event happened. The value defaults to the moment this function is invoked.
     * @param id - The unique identifier of the event. [randomId] by default.
     */
    fun log(
        message: CharSequence,
        label: String? = null,
        payload: CharSequence? = null,
        isPersisted: Boolean = false,
        timestamp: Long = currentTimestamp,
        id: String = randomId
    ) = Unit

    /**
     * Clears all log messages for the specified tag.
     *
     * @param label - A specific tag to filter out, or null to delete all logs. Null by default.
     */
    fun clearLogs(label: String? = null) = Unit

    /**
     * For internal use only.
     */
    fun register(
        onNewLog: (
            message: CharSequence,
            label: String?,
            payload: CharSequence?,
            isPersisted: Boolean,
            timestamp: Long,
            id: String
        ) -> Unit,
        clearLogs: (label: String?) -> Unit
    ) = Unit
}