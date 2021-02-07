package com.pandulapeter.beagle.common.listeners

import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry

/**
 * Implement this interface to get notified about when a new network log entry is added using Beagle.logNetworkEvent().
 */
interface NetworkLogListener {

    /**
     * Called when the new network log entry is added.
     *
     * @param networkLogEntry - The newly added [NetworkLogEntry].
     */
    fun onNetworkLogEntryAdded(networkLogEntry: NetworkLogEntry)
}