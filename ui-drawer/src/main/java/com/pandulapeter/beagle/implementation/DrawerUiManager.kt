package com.pandulapeter.beagle.implementation

import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuView
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

internal class DrawerUiManager : UiManagerContract {

    private val drawers = mutableMapOf<FragmentActivity, DebugMenuView>()

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            Beagle.currentActivity?.let { hide(it) }
        }
    }

    //TODO: State restoration
    override fun injectOverlayFrameLayout(activity: FragmentActivity, oldRootViewGroup: ViewGroup, overlayFrameLayout: OverlayFrameLayout) {
        drawers[activity] = injectDrawerLayout(oldRootViewGroup, overlayFrameLayout)
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    override fun onActivityDestroyed(activity: FragmentActivity) {
        drawers.remove(activity)
    }

    override fun show(activity: FragmentActivity) = (Beagle.isUiEnabled && drawers.containsKey(activity)).also {
        drawers[activity]!!.run {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
            (parent as DebugMenuDrawerLayout).openDrawer(this)
        }
    }

    override fun hide(activity: FragmentActivity): Boolean {
        val drawer = drawers[activity]
        val drawerLayout = drawer?.parent as? DebugMenuDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            drawerLayout?.closeDrawers()
        }
    }

    private fun updateDrawerLockMode() = drawers.values.forEach { drawer ->
        (drawer.parent as? DebugMenuDrawerLayout?)?.setDrawerLockMode(if (Beagle.isUiEnabled) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun injectDrawerLayout(rootViewGroup: ViewGroup, overlayFrameLayout: OverlayFrameLayout) =
        DebugMenuView(BeagleCore.implementation.getThemedContext(overlayFrameLayout.context)).also { drawer ->
            rootViewGroup.run {
                post {
                    val oldViews = (0 until childCount).map { getChildAt(it) }
                    removeAllViews()
                    oldViews.forEach { overlayFrameLayout.addView(it) }
                    addView(
                        DebugMenuDrawerLayout(
                            context = overlayFrameLayout.context,
                            drawer = drawer,
                            content = overlayFrameLayout
                        ).apply {
                            updateDrawerLockMode()
                            post { updateDrawerLockMode() }
                            addDrawerListener(object : DrawerLayout.DrawerListener {

                                override fun onDrawerStateChanged(newState: Int) = Unit

                                override fun onDrawerSlide(drawerView: View, slideOffset: Float) = BeagleCore.implementation.hideKeyboard()

                                override fun onDrawerClosed(drawerView: View) {
                                    onBackPressedCallback.isEnabled = false
                                }

                                override fun onDrawerOpened(drawerView: View) {
                                    onBackPressedCallback.isEnabled = true
                                }
                            })
                        },
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        }
}