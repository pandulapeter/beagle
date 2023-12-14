package com.pandulapeter.beagle.core.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.OverlayFragment
import com.pandulapeter.beagle.core.util.SimpleActivityLifecycleCallbacks
import com.pandulapeter.beagle.core.util.extension.supportsDebugMenu
import com.pandulapeter.beagle.core.view.gallery.MediaPreviewDialogFragment
import com.pandulapeter.beagle.core.view.logDetail.LogDetailDialogFragment
import com.pandulapeter.beagle.core.view.networkLogDetail.NetworkLogDetailDialogFragment
import com.pandulapeter.beagle.modules.LifecycleLogListModule

internal class DebugMenuInjector(
    private val uiManager: UiManagerContract
) {
    var currentActivity: FragmentActivity? = null
        private set(value) {
            field = value
            if (value != null) {
                injectDebugMenu(value.supportFragmentManager.findFragmentById(android.R.id.content))
            }
        }

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.FRAGMENT_ON_ATTACH)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.FRAGMENT_ON_ACTIVITY_CREATED, savedInstanceState != null)
            }
        }

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_CREATE, savedInstanceState != null)
            }
        }

        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            if (f.shouldLogFragment()) {
                injectDebugMenu(f)
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.FRAGMENT_ON_VIEW_CREATED, savedInstanceState != null)
            }
        }

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_START)
            }
        }

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_RESUME)
            }
        }

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_SAVE_INSTANCE_STATE)
            }
        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_PAUSE)
            }
        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_STOP)
            }
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.FRAGMENT_ON_VIEW_DESTROYED)
            }
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.ON_DESTROY)
            }
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            if (f.shouldLogFragment()) {
                BeagleCore.implementation.logLifecycle(f::class.java, LifecycleLogListModule.EventType.FRAGMENT_ON_DETACH)
            }
        }

    }
    private val activityLifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity.supportsDebugMenu) {
                (activity as FragmentActivity).supportFragmentManager.run {
                    unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
                    registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)
                }
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_CREATE, savedInstanceState != null)
            }
        }

        override fun onActivityStarted(activity: Activity) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_START)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            if (currentActivity != activity) {
                currentActivity = if (activity.supportsDebugMenu || BeagleCore.implementation.uiManager.isActivityDebugMenu(activity)) activity as FragmentActivity else null
                BeagleCore.implementation.refresh()
            }
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_RESUME)
                if (activity.isInPictureInPictureMode) {
                    BeagleCore.implementation.hide()
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_SAVE_INSTANCE_STATE)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_PAUSE)
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_STOP)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.supportsDebugMenu) {
                if (activity == currentActivity) {
                    currentActivity = null
                }
                BeagleCore.implementation.logLifecycle(activity::class.java, LifecycleLogListModule.EventType.ON_DESTROY)
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

    private fun Fragment.shouldLogFragment() = this !is OverlayFragment
            && this !is MediaPreviewDialogFragment
            && this !is LogDetailDialogFragment
            && this !is NetworkLogDetailDialogFragment
            && !BeagleCore.implementation.uiManager.isFragmentDebugMenu(this)

    private fun injectDebugMenu(currentFragment: Fragment?) {
        currentActivity?.let { currentActivity ->
            if (!BeagleCore.implementation.uiManager.isActivityDebugMenu(currentActivity)) {
                uiManager.addOverlayFragment(currentFragment, currentActivity)
            }
        }
    }
}