package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import com.pandulapeter.debugMenuCore.DebugMenu

object DebugMenu : DebugMenu {

    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            drawers[activity] = createAndAddDrawerLayout(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            drawers.remove(activity)
        }
    }

    override fun initialize(application: Application) {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)

    private fun createAndAddDrawerLayout(activity: Activity) = DebugMenuDrawer(activity).also { drawer ->
        activity.findRootViewGroup().run {
            post {
                val oldViews = (0 until childCount).map { getChildAt(it) }
                removeAllViews()
                addView(
                    DebugMenuDrawerLayout(
                        context = activity,
                        oldViews = oldViews,
                        drawer = drawer
                    ),
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    }
}