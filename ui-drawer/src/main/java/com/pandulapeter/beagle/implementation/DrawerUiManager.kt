package com.pandulapeter.beagle.implementation

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class DrawerUiManager : UiManagerContract {

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            Beagle.currentActivity?.let { hide(it) }
        }
    }

    override fun onActivityCreated(activity: FragmentActivity) {
        //TODO  drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    override fun onActivityDestroyed(activity: FragmentActivity) {
        //TODO   drawers.remove(activity)
    }

    override fun show(activity: FragmentActivity) = false
    //(Beagle.isUiEnabled && drawers.containsKey(activity)).also {
    //TODO     drawers[activity]?.run { (parent as? BeagleDrawerLayout?)?.openDrawer(this) }
//    }

    override fun hide(activity: FragmentActivity): Boolean {
        //TODO     val drawer = drawers[activity]
//        val drawerLayout = drawer?.parent as? BeagleDrawerLayout?
//        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
//            drawerLayout?.closeDrawers()
//        }
        return false
    }

    private fun notifyListenersOnOpened() {
        onBackPressedCallback.isEnabled = true
        //TODO   drawers.values.forEach { drawer -> (drawer.parent as? BeagleDrawerLayout?)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED) }
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    private fun notifyListenersOnClosed() {
        onBackPressedCallback.isEnabled = false
        //TODO:      updateDrawerLockMode()
//        if (shouldTakeScreenshot) {
//            currentActivity?.takeAndShareScreenshot()
//            shouldTakeScreenshot = false
//        }
//        Trick.resetPendingChanges()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
    }
}