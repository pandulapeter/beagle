package com.pandulapeter.beagle.core.manager.listener

import android.graphics.Canvas
import androidx.core.view.WindowInsetsCompat
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.configuration.getBeagleInsets
import com.pandulapeter.beagle.common.listeners.OverlayListener

internal class OverlayListenerManager : BaseListenerManager<OverlayListener>() {

    fun notifyListeners(canvas: Canvas) {
        val insets =
            BeagleCore.implementation.currentActivity?.window?.decorView?.let { view ->
                view.rootWindowInsets?.let {
                    WindowInsetsCompat.toWindowInsetsCompat(it, view).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                } ?: Insets()
            } ?: Insets()
        notifyListeners { it.onDrawOver(canvas, insets) }
    }

    override fun addListener(listener: OverlayListener) {
        super.addListener(listener)
        BeagleCore.implementation.invalidateOverlay()
    }

    override fun onListenerRemoved() = BeagleCore.implementation.invalidateOverlay()
}