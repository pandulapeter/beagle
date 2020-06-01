package com.pandulapeter.beagle.core.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.BeagleCore

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class OverlayFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.let { BeagleCore.implementation.notifyOverdrawListenersDrawOver(canvas) }
    }
}