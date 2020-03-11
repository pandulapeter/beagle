package com.pandulapeter.beagle.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.dimension

internal class BeagleDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    oldViews: List<View> = emptyList(),
    drawer: View? = null,
    @Dimension drawerWidth: Int? = null
) : DrawerLayout(context, attrs, defStyleAttr) {

    private val container = OverlayFrameLayout(context)
    var keylineOverlay
        get() = container.keylineOverlayToggle
        set(value) {
            container.keylineOverlayToggle = value
        }
    var viewBoundsOverlay
        get() = container.viewBoundsOverlayToggle
        set(value) {
            container.viewBoundsOverlayToggle = value
        }
    private val listener = object : DrawerListener {

        override fun onDrawerOpened(drawerView: View) = Beagle.notifyListenersOnOpened()

        override fun onDrawerClosed(drawerView: View) = Beagle.notifyListenersOnClosed()

        override fun onDrawerStateChanged(newState: Int) {
            if (newState == STATE_DRAGGING && drawer?.let { isDrawerOpen(it) } != true) {
                Beagle.notifyListenersOnDragStarted()
            }
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDrawerLockMode(if (Beagle.isEnabled) LOCK_MODE_UNDEFINED else LOCK_MODE_LOCKED_CLOSED)
        addDrawerListener(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeDrawerListener(listener)
    }


    init {
        addView(container.apply { oldViews.forEach { view -> addView(view) } }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(drawer, LayoutParams(drawerWidth ?: context.dimension(R.dimen.beagle_default_drawer_width), LayoutParams.MATCH_PARENT, GravityCompat.END))
    }
}
