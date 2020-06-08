package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.manager.listener.BaseListenerManager

internal class VisibilityListenerManager : BaseListenerManager<VisibilityListener>() {

    //TODO: The way visibility listeners are notified is buggy during configuration changes
    private var isDebugMenuVisible = false

    fun notifyVisibilityListenersOnShow() {
        if (!isDebugMenuVisible) {
            notifyListeners { it.onShown() }
            isDebugMenuVisible = true
        }
    }

    fun notifyVisibilityListenersOnHide() {
        if (isDebugMenuVisible) {
            notifyListeners { it.onHidden() }
            isDebugMenuVisible = false
        }
    }
}