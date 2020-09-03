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

internal class DebugMenuInjector(
    private val uiManager: UiManagerContract
) {
    var currentActivity: FragmentActivity? = null
        private set
    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onCreate(savedInstanceState ${if (savedInstanceState == null) "=" else "!="} null)")
            }
        }

        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onCreateView(savedInstanceState ${if (savedInstanceState == null) "=" else "!="} null)")
            }
        }

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onAttach()")
            }
        }

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onStart()")
            }
        }

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onResume()")
            }
        }

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onFragmentSaveInstanceState()")
            }
        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onPause()")
            }
        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onStop()")
            }
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onDetach()")
            }
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onDestroyView()")
            }
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            if (f !is OverlayFragment) {
                BeagleCore.implementation.logLifecycle(f::class.java, "onDestroy()")
            }
        }

    }
    private val activityLifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity.supportsDebugMenu) {
                if (savedInstanceState == null) {
                    uiManager.addOverlayFragment(activity as FragmentActivity)
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)
                }
                BeagleCore.implementation.logLifecycle(activity::class.java, "onCreate(savedInstanceState ${if (savedInstanceState == null) "=" else "!="} null)")
            }
        }

        override fun onActivityStarted(activity: Activity) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, "onStart()")
            }
        }

        override fun onActivityResumed(activity: Activity) {
            if (currentActivity != activity) {
                currentActivity = if (activity.supportsDebugMenu) activity as FragmentActivity else null
                BeagleCore.implementation.refresh()
            }
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, "onResume()")
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, "onSaveInstanceState()")
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, "onPause()")
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (activity.supportsDebugMenu) {
                BeagleCore.implementation.logLifecycle(activity::class.java, "onStop()")
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity.supportsDebugMenu) {
                if (activity == currentActivity) {
                    currentActivity = null
                }
                BeagleCore.implementation.logLifecycle(activity::class.java, "onDestroy()")
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