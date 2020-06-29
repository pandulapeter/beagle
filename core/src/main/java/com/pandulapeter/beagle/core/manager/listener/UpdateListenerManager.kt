package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.UpdateListener

internal class UpdateListenerManager : BaseListenerManager<UpdateListener>() {

    fun notifyListeners() = notifyListeners { it.onContentsChanged() }
}