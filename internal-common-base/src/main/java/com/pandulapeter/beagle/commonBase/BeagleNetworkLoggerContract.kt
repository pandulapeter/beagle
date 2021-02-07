package com.pandulapeter.beagle.commonBase

import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry

interface BeagleNetworkLoggerContract {

    /**
     * Cast this value to a nullable OkHttp Interceptor or Ktor Logger depending on the Beagle log dependency you're using.
     * By doing so the library will automatically intercept all network events.
     */
    val logger: Any? get() = null

    /**
     * Adds a new network log message handled by NetworkLogListModule and notifies the registered NetworkLogListeners.
     *
     * @param isOutgoing - True for requests, false for responses.
     * @param url - The complete URL of the endpoint. This will appear in the log list as the title of the entry.
     * @param payload - The payload String of the request or null if not applicable. This will appear in the dialog when the user selects the entry. JSON strings will automatically be formatted.
     * @param headers - The request headers, or null if not applicable. Null by default.
     * @param duration - The duration of the event, or null if not applicable. Null by default.
     * @param timestamp - The moment the event happened. The value defaults to the moment this function is invoked.
     * @param id - The unique identifier of the event. [randomId] by default.
     */
    fun logNetwork(
        isOutgoing: Boolean,
        url: String,
        payload: String?,
        headers: List<String>? = null,
        duration: Long? = null,
        timestamp: Long = currentTimestamp,
        id: String = randomId
    ) = Unit

    /**
     * Clears all network log messages.
     */
    fun clearNetworkLogEntries() = Unit

    /**
     * For internal use only.
     */
    fun register(
        onNewLog: (NetworkLogEntry) -> Unit,
        clearLogs: () -> Unit
    ) = Unit
}