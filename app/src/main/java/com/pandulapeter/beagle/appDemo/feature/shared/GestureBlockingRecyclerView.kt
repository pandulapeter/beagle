package com.pandulapeter.beagle.appDemo.feature.shared

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class GestureBlockingRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    var shouldBlockGestures = false

    override fun canScrollVertically(direction: Int) = !shouldBlockGestures && super.canScrollVertically(direction)

    override fun dispatchTouchEvent(ev: MotionEvent?) = if (shouldBlockGestures) false else super.dispatchTouchEvent(ev)

    override fun onInterceptTouchEvent(ev: MotionEvent?) = if (shouldBlockGestures) false else try {
        super.onInterceptTouchEvent(ev)
    } catch (_: IllegalArgumentException) {
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?) = if (shouldBlockGestures) false else super.onTouchEvent(ev)
}