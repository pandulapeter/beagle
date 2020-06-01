package com.pandulapeter.beagle.core.manager

import android.graphics.Canvas
import com.pandulapeter.beagle.common.listeners.OverdrawListener

internal class OverdrawListenerManager : ListenerManager<OverdrawListener>() {

    fun notifyOverdrawListenersDrawOver(canvas: Canvas) = notifyListeners { it.drawOver(canvas) }
}