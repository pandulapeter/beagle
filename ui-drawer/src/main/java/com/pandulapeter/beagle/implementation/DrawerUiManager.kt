package com.pandulapeter.beagle.implementation

import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

internal class DrawerUiManager : UiManagerContract {

    private val drawers = mutableMapOf<FragmentActivity, BeagleDrawer>()

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            Beagle.currentActivity?.let { hide(it) }
        }
    }

    override fun injectOverlayFrameLayout(activity: FragmentActivity, oldRootViewGroup: ViewGroup, overlayFrameLayout: OverlayFrameLayout) {
        drawers[activity] = injectDrawerLayout(activity, oldRootViewGroup, overlayFrameLayout)
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    override fun onActivityDestroyed(activity: FragmentActivity) {
        drawers.remove(activity)
    }

    override fun show(activity: FragmentActivity) = (Beagle.isUiEnabled && drawers.containsKey(activity)).also {
        drawers[activity]!!.run {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
            (parent as BeagleDrawerLayout).openDrawer(this)
        }
    }

    override fun hide(activity: FragmentActivity): Boolean {
        val drawer = drawers[activity]
        val drawerLayout = drawer?.parent as? BeagleDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            drawerLayout?.closeDrawers()
        }
    }

    private fun updateDrawerLockMode() = drawers.values.forEach { drawer ->
        (drawer.parent as? BeagleDrawerLayout?)?.setDrawerLockMode(if (Beagle.isUiEnabled) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    //TODO: Make sure this doesn't break Activity shared element transitions.
    private fun injectDrawerLayout(activity: FragmentActivity, rootViewGroup: ViewGroup, overlayFrameLayout: OverlayFrameLayout) =
        (BeagleCore.implementation.appearance.themeResourceId?.let { ContextThemeWrapper(activity, it) } ?: activity).let { themedContext ->
            BeagleDrawer(themedContext).also { drawer ->
                rootViewGroup.run {
                    post {
                        val oldViews = (0 until childCount).map { getChildAt(it) }
                        removeAllViews()
                        oldViews.forEach { overlayFrameLayout.addView(it) }
                        addView(
                            BeagleDrawerLayout(
                                context = themedContext,
                                drawer = drawer,
                                content = overlayFrameLayout
                            ).apply {
                                updateDrawerLockMode()
                                post { updateDrawerLockMode() }
                                addDrawerListener(object : DrawerLayout.DrawerListener {

                                    override fun onDrawerStateChanged(newState: Int) = Unit

                                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit  //TODO activity.currentFocus?.hideKeyboard() ?: Unit

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
}