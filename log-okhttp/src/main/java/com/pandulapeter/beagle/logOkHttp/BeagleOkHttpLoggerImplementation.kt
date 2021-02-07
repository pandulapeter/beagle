package com.pandulapeter.beagle.logOkHttp

import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract
import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry
import okhttp3.Interceptor

internal class BeagleOkHttpLoggerImplementation : BeagleNetworkLoggerContract {

    private var onNewLog: ((NetworkLogEntry) -> Unit)? = null
    private var clearLogs: (() -> Unit)? = null
    override val logger: Interceptor by lazy { OkHttpInterceptor() }

    override fun logNetwork(
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

    override fun clearNetworkLogEntries() {
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