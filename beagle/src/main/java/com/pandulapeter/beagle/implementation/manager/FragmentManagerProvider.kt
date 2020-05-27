package com.pandulapeter.beagle.implementation.manager

import android.app.Activity
import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.implementation.util.SimpleActivityLifecycleCallbacks

internal class FragmentManagerProvider {

    val fragmentManager get() = currentActivity?.supportFragmentManager
    private var currentActivity: FragmentActivity? = null
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            if (currentActivity != activity) {
                Beagle.hide()
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