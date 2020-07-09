package com.pandulapeter.beagle.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
internal open class SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityResumed(activity: Activity) = Unit
}