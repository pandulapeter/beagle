package com.pandulapeter.beagle.core.manager.listener

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

internal abstract class BaseListenerManager<T> {

    private val listeners = mutableListOf<T>()
    private val internalListeners = mutableListOf<T>()

    fun addInternalListener(listener: T) {
        synchronized(internalListeners) {
            if (!internalListeners.contains(listener)) {
                internalListeners.add(listener)
            }
        }
    }

    @Suppress("unused")
    fun addListener(listener: T, lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = addListener(listener)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeListener(listener)
                lifecycleOwner.lifecycle.removeObserver(this)
                onListenerRemoved()
            }
        }) ?: addListener(listener)
    }

    fun removeListener(listener: T) {
        synchronized(internalListeners) { internalListeners.remove(listener) }
        synchronized(listeners) { listeners.remove(listener) }
        onListenerRemoved()
    }

    fun clearListeners() {
        synchronized(listeners) { listeners.clear() }
        onListenerRemoved()
    }

    protected fun notifyListeners(notification: (T) -> Unit) {
        synchronized(internalListeners) { internalListeners.forEach(notification) }
        synchronized(listeners) { listeners.forEach(notification) }
    }

    @CallSuper
    protected open fun addListener(listener: T) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }
        }
    }

    protected open fun onListenerRemoved() = Unit
}