package com.pandulapeter.beagle.logKtor

import io.ktor.client.features.logging.Logger

//TODO: Work in progress
internal class NetworkInterceptor(
    private val logNetworkEvent: () -> ((isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long) -> Unit)?
) : Logger {

    override fun log(message: String) {
        logNetworkEvent()?.invoke(
            true,
            "",
            message,
            null,
            null,
            System.currentTimeMillis()
        )
    }
}