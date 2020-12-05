package com.pandulapeter.beagle.logCrash.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.configuration.Insets
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
    } else (((width - 2 * ViewConfiguration.get(context).scaledTouchSlop) <= ev.x) && super.onTouchEvent(ev))

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDrawerLockMode(if (Beagle.isUiEnabled) LOCK_MODE_UNDEFINED else LOCK_MODE_LOCKED_CLOSED)
        addDrawerListener(listener)
        setDrawerSize(DisplayMetrics())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeDrawerListener(listener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            debugMenuView.setOnApplyWindowInsetsListener(null)
        }
    }

    private fun setDrawerSize(displayMetrics: DisplayMetrics) {
        BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        if (displayMetrics.widthPixels == 0) {
            post { setDrawerSize(displayMetrics) }
        } else {
            debugMenuView.run {
                layoutParams = layoutParams.apply {
                    width = min(resources.getDimensionPixelSize(R.dimen.beagle_drawer_maximum_width), (displayMetrics.widthPixels * DRAWER_WIDTH_RATIO).roundToInt())
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                debugMenuView.run {
                    setOnApplyWindowInsetsListener { _, insets -> insets.also(::updateInsets) }
                    requestApplyInsets()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    private fun updateInsets(insets: WindowInsets) {
        val input = Insets(
            left = insets.systemWindowInsetLeft,
            top = insets.systemWindowInsetTop,
            right = insets.systemWindowInsetRight,
            bottom = insets.systemWindowInsetBottom
        )
        val output = (BeagleCore.implementation.appearance.applyInsets?.invoke(input) ?: input)
        debugMenuView.applyInsets(output.left, output.top, output.right, output.bottom)
    }

    companion object {
        private const val DRAWER_WIDTH_RATIO = 0.6f
    }
}