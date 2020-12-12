package com.pandulapeter.beagle.core.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ScrollView
import kotlin.math.abs

internal class TolerantScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var lastX: Int = 0
    private var lastY: Int = 0
    private var distanceX: Int = 0
    private var distanceY: Int = 0
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        onTouchEvent(ev)
        val dy: Int
        val dx: Int
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                distanceX = 0
                distanceY = 0
                lastX = ev.x.toInt()
                lastY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                dx = abs((lastX - ev.x).toInt())
                lastX = ev.x.toInt()
                distanceX += dx
                dy = abs((lastY - ev.y).toInt())
                lastY = ev.y.toInt()
                distanceY += dy
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (touchSlop in (distanceX + 1) until distanceY) {
                    return true
                }
                distanceX = 0
                distanceY = 0
            }
        }
        return false
    }
}