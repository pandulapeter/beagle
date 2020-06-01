package com.pandulapeter.beagle.core.manager

import android.graphics.Canvas
import com.pandulapeter.beagle.common.listeners.OverlayListener

internal class OverlayListenerManager : ListenerManager<OverlayListener>() {

    fun notifyOverlayListenersOnDrawOver(canvas: Canvas) = notifyListeners { it.onDrawOver(canvas) }
}