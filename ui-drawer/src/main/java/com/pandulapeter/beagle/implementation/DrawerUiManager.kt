package com.pandulapeter.beagle.implementation

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuView
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class DrawerUiManager : UiManagerContract {

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            Beagle.currentActivity?.let { hide(it) }
        }
    }

    override fun addOverlayFragment(activity: FragmentActivity) {
        super.addOverlayFragment(activity)
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    override fun createOverlayLayout(context: Context) = DebugMenuView(BeagleCore.implementation.getThemedContext(context)).let { drawer ->
        DebugMenuDrawerLayout(
            context = context,
            drawer = drawer
        ).apply {
            updateDrawerLockMode()
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
        }
    }

    override fun findOverlayView(activity: FragmentActivity?): View? = getDrawerLayout(activity)?.getChildAt(0)

    override fun show(activity: FragmentActivity) = (Beagle.isUiEnabled).also {
        getDrawerView(activity)?.run {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
            (parent as DebugMenuDrawerLayout).openDrawer(this)
        }
    }

    override fun hide(activity: FragmentActivity): Boolean {
        val drawer = getDrawerView(activity)
        val drawerLayout = drawer?.parent as? DebugMenuDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            drawerLayout?.closeDrawers()
        }
    }

    private fun getDrawerView(activity: FragmentActivity?) = getDrawerLayout(activity)?.drawer

    private fun getDrawerLayout(activity: FragmentActivity?) = findOverlayFragment(activity)?.view as? DebugMenuDrawerLayout?

    private fun DebugMenuDrawerLayout.updateDrawerLockMode() = setDrawerLockMode(if (Beagle.isUiEnabled) DrawerLayout.LOCK_MODE_UNDEFINED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}