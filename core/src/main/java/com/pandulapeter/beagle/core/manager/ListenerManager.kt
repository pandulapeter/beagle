package com.pandulapeter.beagle.core.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

internal abstract class ListenerManager<T> {

    private val listeners = mutableListOf<T>()

    @Suppress("unused")
    fun addListener(listener: T, lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = addListener(listener)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeListener(listener)
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: addListener(listener)
    }

    fun removeListener(listener: T) {
        this.listeners.remove(listener)
    }

    fun clearListeners() = listeners.clear()

    protected fun notifyListeners(notification: (T) -> Unit) = listeners.forEach(notification)

    private fun addListener(listener: T) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }
}