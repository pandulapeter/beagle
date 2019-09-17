package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.debugMenuCore.DebugMenu

object DebugMenu : DebugMenu {

    private val drawers = mutableMapOf<Activity, View>()
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

    private fun createAndAddDrawerLayout(activity: Activity): View {
        val oldRoot = activity.findRootViewGroup()
        val drawer = activity.createDrawer()
        oldRoot.post {
            val oldViews = oldRoot.run { (0 until childCount).map { getChildAt(it) } }
            oldRoot.removeAllViews()
            oldRoot.addView(DrawerLayout(activity).apply {
                addView(
                    FrameLayout(activity).apply { oldViews.forEach { view -> addView(view) } },
                    DrawerLayout.LayoutParams(
                        DrawerLayout.LayoutParams.MATCH_PARENT,
                        DrawerLayout.LayoutParams.MATCH_PARENT
                    )
                )
                addView(
                    drawer,
                    DrawerLayout.LayoutParams(
                        DrawerLayout.LayoutParams.WRAP_CONTENT,
                        DrawerLayout.LayoutParams.MATCH_PARENT, GravityCompat.END
                    )
                )
            }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        return drawer
    }

    private fun Context.createDrawer() = TextView(this).apply {
        text = "Debug menu"
        setBackgroundColor(Color.RED)
        setTextColor(Color.CYAN)
        100.let { setPadding(it, it, it, it) }
    }
}