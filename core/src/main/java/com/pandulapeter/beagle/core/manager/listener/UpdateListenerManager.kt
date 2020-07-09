package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.UpdateListener

internal class UpdateListenerManager : BaseListenerManager<UpdateListener>() {

    fun notifyListenersOnContentsChanged() = notifyListeners { it.onContentsChanged() }

    fun notifyListenersOnAllPendingChangesApplied() = notifyListeners { it.onAllPendingChangesApplied() }
}