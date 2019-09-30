package com.pandulapeter.beagle.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.colorResource
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagleCore.configuration.tricks.KeylineOverlayToggleTrick

internal class KeylineOverlayFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var keylineOverlayToggle: KeylineOverlayToggleTrick? = null
        set(value) {
            field = value
            if (value != null) {
                gridPaint.color = value.gridColor ?: context.colorResource(android.R.attr.textColorPrimary)
                gridPaint.alpha = GRID_ALPHA
                keylinePaint.color = value.gridColor ?: context.colorResource(android.R.attr.textColorPrimary)
                keylinePaint.alpha = KEYLINE_ALPHA
                keylineGrid = value.keylineGrid ?: context.dimension(R.dimen.keyline_grid)
                keylinePrimary = (value.keylinePrimary ?: context.dimension(R.dimen.keyline_primary)).toFloat()
                keylineSecondary = (value.keylineSecondary ?: context.dimension(R.dimen.keyline_secondary)).toFloat()
            }
            invalidate()
        }
    private val gridPaint = Paint()
    private val keylinePaint = Paint()
    private var keylineGrid = 0
    private var keylinePrimary = 0f
    private var keylineSecondary = 0f

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.run {
            if (keylineOverlayToggle != null) {
                for (x in 0..width step keylineGrid) {
                    drawLine(x.toFloat(), 0f, x.toFloat(), height.toFloat(), gridPaint)
                }
                for (y in 0..height step keylineGrid) {
                    drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), gridPaint)
                }
                drawLine(keylinePrimary, 0f, keylinePrimary, height.toFloat(), keylinePaint)
                drawLine(keylineSecondary, 0f, keylineSecondary, height.toFloat(), keylinePaint)
                drawLine(width - keylinePrimary, 0f, width - keylinePrimary, height.toFloat(), keylinePaint)
            }
        }
    }

    companion object {
        private const val GRID_ALPHA = 64
        private const val KEYLINE_ALPHA = 192
    }
}
