package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.NetworkLogListener
import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry

internal class NetworkLogListenerManager : BaseListenerManager<NetworkLogListener>() {

    fun notifyListeners(networkLogEntry: NetworkLogEntry) = notifyListeners { it.onNetworkLogEntryAdded(networkLogEntry) }
}