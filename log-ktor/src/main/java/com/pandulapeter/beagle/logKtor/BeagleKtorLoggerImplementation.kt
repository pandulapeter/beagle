package com.pandulapeter.beagle.logKtor

import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract
import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry
import io.ktor.client.features.HttpClientFeature

internal class BeagleKtorLoggerImplementation : BeagleNetworkLoggerContract {

    private var onNewLog: ((NetworkLogEntry) -> Unit)? = null
    private var clearLogs: (() -> Unit)? = null
    override val logger: HttpClientFeature<*, *> = KtorLogger

    override fun logNetworkEvent(
        isOutgoing: Boolean,
        url: String,
        payload: String?,
        headers: List<String>?,
        duration: Long?,
        timestamp: Long,
        id: String
    ) {
        onNewLog?.invoke(
            NetworkLogEntry(
                id = id,
                isOutgoing = isOutgoing,
                url = url,
                payload = payload,
                headers = headers.orEmpty(),
                duration = duration,
                timestamp = timestamp,
            )
        )
    }

    override fun clearNetworkLogs() {
        clearLogs?.invoke()
    }

    override fun register(
        onNewLog: (NetworkLogEntry) -> Unit,
        clearLogs: () -> Unit
    ) {
        this.onNewLog = onNewLog
        this.clearLogs = clearLogs
    }
}