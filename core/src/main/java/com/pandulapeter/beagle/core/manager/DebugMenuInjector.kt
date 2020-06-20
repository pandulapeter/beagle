package com.pandulapeter.beagle.core.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.SimpleActivityLifecycleCallbacks
import com.pandulapeter.beagle.core.util.extension.supportsDebugMenu

internal class DebugMenuInjector(
    private val uiManager: UiManagerContract
) {

    var currentActivity: FragmentActivity? = null
        private set
    private val activityLifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity.supportsDebugMenu && savedInstanceState == null) {
                uiManager.addOverlayFragment(activity as FragmentActivity)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            if (currentActivity != activity) {
                currentActivity = if (activity.supportsDebugMenu) activity as FragmentActivity else null
                BeagleCore.implementation.refresh()
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.supportsDebugMenu) {
                if (activity == currentActivity) {
                    currentActivity = null
                }
            }
        }
    }

    internal fun invalidateOverlay() {
        uiManager.findOverlayView(currentActivity)?.postInvalidate()
    }

    internal fun register(application: Application) = application.run {
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
}