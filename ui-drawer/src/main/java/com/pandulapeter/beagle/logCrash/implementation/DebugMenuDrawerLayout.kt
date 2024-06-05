package com.pandulapeter.beagle.logCrash.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowInsets
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.configuration.getBeagleInsets
import com.pandulapeter.beagle.core.view.InternalDebugMenuView
import kotlin.math.min
import kotlin.math.roundToInt


@SuppressLint("ViewConstructor")
internal class DebugMenuDrawerLayout(
    context: Context,
    overlayFrameLayout: View,
    val debugMenuView: InternalDebugMenuView
) : DrawerLayout(context) {

    private val listener = object : DrawerListener {

        override fun onDrawerOpened(drawerView: View) = Unit

        override fun onDrawerClosed(drawerView: View) = BeagleCore.implementation.notifyVisibilityListenersOnHide()

        override fun onDrawerStateChanged(newState: Int) {
            if (newState == STATE_DRAGGING && !isDrawerOpen(debugMenuView)) {
                BeagleCore.implementation.notifyVisibilityListenersOnShow()
            }
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
    }

    init {
        addView(overlayFrameLayout, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        addView(debugMenuView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, GravityCompat.END))
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?) = isDrawerOpen(debugMenuView) && try {
        super.onInterceptTouchEvent(ev)
    } catch (_: NullPointerException) {
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent) = if (isDrawerVisible(debugMenuView) || ev.action != MotionEvent.ACTION_DOWN) {
        super.onTouchEvent(ev)
    } else {
        val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        val isTouchOnEdge = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            // Checking for touch on the right edge for LTR
            (width - 2 * scaledTouchSlop) <= ev.x
        } else {
            // Checking for touch on the left edge for RTL
            ev.x <= 2 * scaledTouchSlop
        }
        isTouchOnEdge && super.onTouchEvent(ev)
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDrawerLockMode(if (isDrawerLocked) LOCK_MODE_LOCKED_CLOSED else LOCK_MODE_UNDEFINED)
        addDrawerListener(listener)
        setDrawerSize(DisplayMetrics())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeDrawerListener(listener)
        debugMenuView.setOnApplyWindowInsetsListener(null)
    }

    private fun setDrawerSize(displayMetrics: DisplayMetrics) {
        BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        if (displayMetrics.widthPixels == 0) {
            post { setDrawerSize(displayMetrics) }
        } else {
            debugMenuView.run {
                layoutParams = layoutParams.apply {
                    displayMetrics.widthPixels.let { screenWidth ->
                        width = BeagleCore.implementation.behavior.getDrawerSize?.invoke(
                            context,
                            screenWidth
                        ) ?: min(resources.getDimensionPixelSize(R.dimen.beagle_drawer_maximum_width), (screenWidth * DRAWER_WIDTH_RATIO).roundToInt())
                    }
                }
            }
            debugMenuView.run {
                setOnApplyWindowInsetsListener { view, insets -> insets.also { updateInsets(it, view) } }
                requestApplyInsets()
            }
        }
    }

    private fun updateInsets(insets: WindowInsets, view: View) {
        val input = WindowInsetsCompat.toWindowInsetsCompat(insets, view).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
        val output = (BeagleCore.implementation.appearance.applyInsets?.invoke(input) ?: input)
        debugMenuView.applyInsets(output.left, output.top, output.right, output.bottom)
    }

    companion object {
        private const val DRAWER_WIDTH_RATIO = 0.6f
    }
}