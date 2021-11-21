package com.pandulapeter.beagle.core.manager.listener

import android.graphics.Canvas
import android.os.Build
import androidx.core.view.WindowInsetsCompat
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.configuration.getBeagleInsets
import com.pandulapeter.beagle.common.listeners.OverlayListener

internal class OverlayListenerManager : BaseListenerManager<OverlayListener>() {

    fun notifyListeners(canvas: Canvas) {
        val insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BeagleCore.implementation.currentActivity?.window?.decorView?.let { view ->
                view.rootWindowInsets?.let {
                    WindowInsetsCompat.toWindowInsetsCompat(it, view).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                } ?: Insets()
            } ?: Insets()
        } else {
            Insets()
        }
        notifyListeners { it.onDrawOver(canvas, insets) }
    }

    override fun addListener(listener: OverlayListener) {
        super.addListener(listener)
        BeagleCore.implementation.invalidateOverlay()
    }

    override fun onListenerRemoved() = BeagleCore.implementation.invalidateOverlay()
}