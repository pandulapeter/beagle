package com.pandulapeter.beagle.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.colorResource
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagleCore.configuration.Trick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

internal class OverlayFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var keylineOverlayToggle: Trick.KeylineOverlayToggle? = null
        set(value) {
            field = value
            if (value != null) {
                gridPaint.color = value.gridColor ?: context.colorResource(android.R.attr.textColorPrimary)
                gridPaint.alpha = GRID_ALPHA
                keylinePaint.color = value.gridColor ?: context.colorResource(android.R.attr.textColorPrimary)
                keylinePaint.alpha = KEYLINE_ALPHA
                keylineGrid = value.keylineGrid ?: context.dimension(R.dimen.beagle_default_keyline_grid)
                keylinePrimary = (value.keylinePrimary ?: context.dimension(R.dimen.beagle_default_keyline_primary)).toFloat()
                keylineSecondary = (value.keylineSecondary ?: context.dimension(R.dimen.beagle_default_keyline_secondary)).toFloat()
            }
            invalidate()
        }

    var viewBoundsOverlayToggle: Trick.ViewBoundsOverlayToggle? = null
        set(value) {
            field = value
            if (value != null) {
                boundsPaint.color = value.color ?: context.colorResource(android.R.attr.textColorPrimary)
                paddingPaint.color = value.color ?: context.colorResource(android.R.attr.textColorPrimary)
                paddingPaint.alpha = FILL_ALPHA
            }
            invalidate()
            startAutomaticRefresh()
        }
    private val gridPaint = Paint()
    private val keylinePaint = Paint()
    private var keylineGrid = 0
    private var keylinePrimary = 0f
    private var keylineSecondary = 0f
    private val boundsPaint = Paint().apply {
        style = Paint.Style.STROKE
    }
    private val paddingPaint = Paint().apply {
        style = Paint.Style.FILL
        alpha = FILL_ALPHA
    }
    private var coroutineContext: CoroutineContext? = null

    private fun startAutomaticRefresh() {
        stopAutomaticRefresh()
        coroutineContext = GlobalScope.launch(Dispatchers.Default) {
            while (true) {
                GlobalScope.launch(Dispatchers.Main) { invalidate() }
                delay(AUTOMATIC_REFRESH_DELAY)
            }
        }
    }

    private fun stopAutomaticRefresh() {
        coroutineContext?.cancel()
        coroutineContext = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (viewBoundsOverlayToggle != null) {
            startAutomaticRefresh()
        }
    }

    override fun onDetachedFromWindow() {
        stopAutomaticRefresh()
        super.onDetachedFromWindow()
    }

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
            if (viewBoundsOverlayToggle != null) {
                drawBoundsIfNeeded(this)
            }
        }
    }

    private fun View.drawBoundsIfNeeded(canvas: Canvas) {
        if (this is ViewGroup) {
            (0 until childCount).forEach {
                getChildAt(it).drawBoundsIfNeeded(canvas)
            }
        } else {
            if (visibility != View.GONE) {
                drawBounds(canvas)
            }
        }
    }

    private fun View.drawBounds(canvas: Canvas) {
        val bounds = Rect()
        getDrawingRect(bounds)
        val location = IntArray(2)
        getLocationOnScreen(location)
        bounds.offset(location[0], location[1])
        bounds.offset(translationX.roundToInt(), translationY.roundToInt())
        canvas.drawRect(bounds, boundsPaint)
        bounds.offset(paddingStart, paddingTop)
        bounds.bottom -= paddingBottom + paddingTop
        bounds.right -= paddingEnd + paddingStart
        canvas.drawRect(bounds, paddingPaint)
    }

    companion object {
        private const val GRID_ALPHA = 64
        private const val KEYLINE_ALPHA = 192
        private const val FILL_ALPHA = 127
        private const val AUTOMATIC_REFRESH_DELAY = 16L
    }
}
