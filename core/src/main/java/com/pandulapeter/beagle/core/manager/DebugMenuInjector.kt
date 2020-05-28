package com.pandulapeter.beagle.core.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.core.util.SimpleActivityLifecycleCallbacks
import com.pandulapeter.beagle.core.util.extension.findRootViewGroup
import com.pandulapeter.beagle.core.util.extension.supportsDebugMenu
import com.pandulapeter.beagle.core.util.extension.themedContext
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

internal class DebugMenuInjector(private val uiManager: UiManagerContract) {

    var currentActivity: FragmentActivity? = null
        private set
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity.supportsDebugMenu) {
                //TODO: BUG: The themedContext should only be used for the debug menu
                uiManager.injectOverlayFrameLayout(activity as FragmentActivity, activity.findRootViewGroup(), OverlayFrameLayout(activity.themedContext))
            }
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            if (currentActivity != activity) {
                currentActivity = if (activity.supportsDebugMenu) activity as FragmentActivity else null
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.supportsDebugMenu) {
                uiManager.onActivityDestroyed(activity as FragmentActivity)
                if (activity == currentActivity) {
                    currentActivity = null
                }
            }
        }
    }

    internal fun register(application: Application) {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }
}