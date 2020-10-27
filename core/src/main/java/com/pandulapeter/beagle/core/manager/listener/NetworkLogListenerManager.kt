package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.NetworkLogListener
import com.pandulapeter.beagle.core.util.model.NetworkLogEntry

internal class NetworkLogListenerManager : BaseListenerManager<NetworkLogListener>() {

    fun notifyListeners(networkLogEntry: NetworkLogEntry) = notifyListeners {
        it.onNetworkEventLogged(
            isOutgoing = networkLogEntry.isOutgoing,
            url = networkLogEntry.url,
            payload = networkLogEntry.payload,
            headers = networkLogEntry.headers,
            duration = networkLogEntry.duration,
            timestamp = networkLogEntry.timestamp
        )
    }
}