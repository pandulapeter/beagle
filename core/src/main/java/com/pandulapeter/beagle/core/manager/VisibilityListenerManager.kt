package com.pandulapeter.beagle.core.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.pandulapeter.beagle.common.contracts.VisibilityListener

internal class VisibilityListenerManager {

    private val visibilityListeners = mutableListOf<VisibilityListener>()
    //TODO: The way visibility listeners are notified is buggy during configuration changes
    private var isDebugMenuVisible = false

    private fun addVisibilityListener(listener: VisibilityListener) {
        if (!visibilityListeners.contains(listener)) {
            visibilityListeners.add(listener)
        }
    }

    fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = addVisibilityListener(listener)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeVisibilityListener(listener)
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: addVisibilityListener(listener)
    }

    fun removeVisibilityListener(listener: VisibilityListener) {
        visibilityListeners.remove(listener)
    }

    fun clearVisibilityListeners() = visibilityListeners.clear()

    fun notifyVisibilityListenersOnShow() {
        if (!isDebugMenuVisible) {
            visibilityListeners.forEach { it.onShown() }
            isDebugMenuVisible = true
        }
    }

    fun notifyVisibilityListenersOnHide() {
        if (isDebugMenuVisible) {
            visibilityListeners.forEach { it.onHidden() }
            isDebugMenuVisible = false
        }
    }
}