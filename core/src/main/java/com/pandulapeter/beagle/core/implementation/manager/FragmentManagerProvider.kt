package com.pandulapeter.beagle.core.implementation.manager

import android.app.Activity
import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.core.implementation.util.SimpleActivityLifecycleCallbacks

internal class FragmentManagerProvider {

    var currentActivity: FragmentActivity? = null
        private set
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            if (currentActivity != activity) {
                currentActivity = activity as? FragmentActivity?
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity == currentActivity) {
                currentActivity = null
            }
        }
    }

    fun register(application: Application) {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }
}