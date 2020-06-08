package com.pandulapeter.beagle.core.manager.listener

import android.graphics.Canvas
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.core.manager.listener.BaseListenerManager

internal class OverlayListenerManager : BaseListenerManager<OverlayListener>() {

    fun notifyOverlayListenersOnDrawOver(canvas: Canvas) = notifyListeners { it.onDrawOver(canvas) }

    override fun addListener(listener: OverlayListener) {
        super.addListener(listener)
        BeagleCore.implementation.invalidateOverlay()
    }
}