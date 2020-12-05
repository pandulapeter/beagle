package com.pandulapeter.beagle.core.manager.listener

import android.graphics.Canvas
import android.os.Build
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.listeners.OverlayListener

internal class OverlayListenerManager : BaseListenerManager<OverlayListener>() {

    fun notifyListeners(canvas: Canvas) {
        var leftInset = 0
        var topInset = 0
        var rightInset = 0
        var bottomInset = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BeagleCore.implementation.currentActivity?.window?.decorView?.rootWindowInsets?.let {
                leftInset = it.systemWindowInsetLeft
                topInset = it.systemWindowInsetTop
                rightInset = it.systemWindowInsetRight
                bottomInset = it.systemWindowInsetBottom
            }
        }
        val insets = Insets(leftInset, topInset, rightInset, bottomInset)
        notifyListeners { it.onDrawOver(canvas, insets) }
    }

    override fun addListener(listener: OverlayListener) {
        super.addListener(listener)
        BeagleCore.implementation.invalidateOverlay()
    }

    override fun onListenerRemoved() = BeagleCore.implementation.invalidateOverlay()
}