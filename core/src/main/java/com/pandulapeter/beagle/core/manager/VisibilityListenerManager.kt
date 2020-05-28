package com.pandulapeter.beagle.core.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.pandulapeter.beagle.shared.contracts.VisibilityListener

internal class VisibilityListenerManager {

    private val visibilityListeners = mutableListOf<VisibilityListener>()

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

    fun notifyVisibilityListenersOnShow() = visibilityListeners.forEach { it.onShown() }

    fun notifyVisibilityListenersOnHide() = visibilityListeners.forEach { it.onHidden() }
}