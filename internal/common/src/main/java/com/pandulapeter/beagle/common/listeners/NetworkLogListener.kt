package com.pandulapeter.beagle.common.listeners

/**
 * Implement this interface to get notified about when a new network log entry is added using Beagle.logNetworkEvent().
 */
interface NetworkLogListener {

    /**
     * Called when the new log message is added.
     *
     * @param isOutgoing - True for requests, false for responses.
     * @param url - The complete URL of the endpoint.
     * @param payload - The payload String of the request or null if not applicable.
     * @param headers - The request headers, or null if not applicable.
     * @param duration - The duration of the event, or null if not applicable.
     * @param timestamp - The moment the event happened.
     */
    fun onNetworkEventLogged(isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long)
}