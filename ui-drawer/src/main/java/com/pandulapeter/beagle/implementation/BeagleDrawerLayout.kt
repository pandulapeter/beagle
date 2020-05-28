package com.pandulapeter.beagle.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

@SuppressLint("ViewConstructor")
internal class BeagleDrawerLayout(
    context: Context,
    content: OverlayFrameLayout,
    drawer: View
) : DrawerLayout(context) {

    private val listener = object : DrawerListener {

        override fun onDrawerOpened(drawerView: View) = Unit

        override fun onDrawerClosed(drawerView: View) = BeagleCore.implementation.notifyVisibilityListenersOnHide()

        override fun onDrawerStateChanged(newState: Int) {
            if (newState == STATE_DRAGGING && !isDrawerOpen(drawer)) {
                BeagleCore.implementation.notifyVisibilityListenersOnShow()
            }
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
    }

    init {
        addView(content, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(drawer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, GravityCompat.END))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDrawerLockMode(if (Beagle.isUiEnabled) LOCK_MODE_UNDEFINED else LOCK_MODE_LOCKED_CLOSED)
        addDrawerListener(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeDrawerListener(listener)
    }
}