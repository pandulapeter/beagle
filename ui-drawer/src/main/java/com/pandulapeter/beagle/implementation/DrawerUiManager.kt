package com.pandulapeter.beagle.implementation

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.util.runOnUiThread
import com.pandulapeter.beagle.core.view.InternalDebugMenuView

internal class DrawerUiManager : UiManagerContract, DrawerLayout.DrawerListener {

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            Beagle.currentActivity?.let { hide(it) }
        }
    }

    override fun createOverlayLayout(activity: FragmentActivity) = InternalDebugMenuView(activity).let { drawer ->
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
        DebugMenuDrawerLayout(
            context = activity,
            overlayFrameLayout = super.createOverlayLayout(activity),
            debugMenuView = drawer
        ).apply {
            updateDrawerLockMode()
            if (onBackPressedCallback.isEnabled) {
                openDrawer(drawer, false)
            }
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) = addDrawerListener(this@DrawerUiManager)

                override fun onStop(owner: LifecycleOwner) = removeDrawerListener(this@DrawerUiManager)
            })
        }
    }

    override fun findOverlayView(activity: FragmentActivity?): View? = getDrawerLayout(activity)?.getChildAt(0)

    override fun show(activity: FragmentActivity) = if (Beagle.isUiEnabled) {
        getDrawerView(activity)?.let { drawer ->
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
            (drawer.parent as DebugMenuDrawerLayout).let { drawerLayout ->
                drawerLayout.isDrawerVisible(drawer).let { isDrawerOpen ->
                    runOnUiThread { drawerLayout.openDrawer(drawer) }
                    !isDrawerOpen
                }
            }
        } ?: false
    } else false

    override fun hide(activity: FragmentActivity?): Boolean {
        val drawer = getDrawerView(activity)
        val drawerLayout = drawer?.parent as? DebugMenuDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            BeagleCore.implementation
            runOnUiThread { drawerLayout?.closeDrawers() }
        }
    }

    override fun onDrawerStateChanged(newState: Int) = Unit

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        BeagleCore.implementation.hideKeyboard()
        onBackPressedCallback.isEnabled = slideOffset != 0f
    }

    override fun onDrawerClosed(drawerView: View) {
        onBackPressedCallback.isEnabled = false
    }

    override fun onDrawerOpened(drawerView: View) {
        onBackPressedCallback.isEnabled = true
    }

    private fun getDrawerView(activity: FragmentActivity?) = getDrawerLayout(activity)?.debugMenuView

    private fun getDrawerLayout(activity: FragmentActivity?) = findOverlayFragment(activity)?.view as? DebugMenuDrawerLayout?

    private fun DebugMenuDrawerLayout.updateDrawerLockMode() = setDrawerLockMode(if (Beagle.isUiEnabled) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}