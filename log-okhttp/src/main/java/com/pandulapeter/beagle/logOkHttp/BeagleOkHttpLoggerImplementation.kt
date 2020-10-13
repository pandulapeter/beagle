package com.pandulapeter.beagle.logOkHttp

import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract
import okhttp3.Interceptor

internal class BeagleOkHttpLoggerImplementation : BeagleNetworkLoggerContract {

    private var onNewLog: ((isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long, id: String) -> Unit)? = null
    private var clearLogs: (() -> Unit)? = null
    override val logger: Interceptor by lazy { OkHttpInterceptor() }

    override fun logNetworkEvent(
        isOutgoing: Boolean,
        url: String,
        payload: String?,
        headers: List<String>?,
        duration: Long?,
        timestamp: Long,
        id: String
    ) {
        onNewLog?.invoke(isOutgoing, url, payload, headers, duration, timestamp, id)
    }

    override fun clearNetworkLogs() {
        clearLogs?.invoke()
    }

    override fun register(
        onNewLog: (isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long, id: String) -> Unit,
        clearLogs: () -> Unit
    ) {
        this.onNewLog = onNewLog
        this.clearLogs = clearLogs
    }
}