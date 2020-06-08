package com.pandulapeter.beagle.core.manager.listener

import com.pandulapeter.beagle.common.listeners.LogListener

internal class LogListenerManager : BaseListenerManager<LogListener>() {

    fun notifyOverlayListenersOnLogEntry(tag: String?, message: String, payload: String?) = notifyListeners { it.onLogMessageAdded(tag, message, payload) }
}