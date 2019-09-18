package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.SimpleActivityLifecycleCallbacks
import com.pandulapeter.debugMenu.views.DebugMenuDrawer
import com.pandulapeter.debugMenu.views.DebugMenuDrawerLayout
import com.pandulapeter.debugMenuCore.DebugMenu
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration

/**
 * The main singleton that handles the debug drawer's functionality.
 */
object DebugMenu : DebugMenu {

    //region Public API
    /**
     * Hooks up the library to the Application's lifecycle. After this is called, a debug drawer will be inserted into every activity. This should be called
     * in the Application's onCreate() method.
     * @param application - The [Application] instance.
     * @param configuration - The [DebugMenuConfiguration] that specifies the appearance and contents of the drawer.
     */
    override fun initialize(application: Application, configuration: DebugMenuConfiguration) {
        this.configuration = configuration
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    /**
     * Tries to open the current Activity's debug drawer.
     * @param activity - The current [Activity] instance.
     */
    override fun openDrawer(activity: Activity) {
        drawers[activity]?.run { (parent as? DebugMenuDrawerLayout?)?.openDrawer(this) }
    }

    /**
     * Tries to close the current Activity's debug drawer. For proper UX this should be used in onBackPressed() to block any other logic if it returns true.
     * @param activity - The current [Activity] instance.
     * @return - True if the drawer was open, false otherwise
     */
    override fun closeDrawer(activity: Activity): Boolean {
        val drawer = drawers[activity]
        val drawerLayout = drawer?.parent as? DebugMenuDrawerLayout?
        return (drawerLayout?.isDrawerOpen(drawer) == true).also {
            drawerLayout?.closeDrawers()
        }
    }
    //endregion

    //region Implementation details
    private var Bundle.isDrawerOpen by BundleArgumentDelegate.Boolean("isDrawerOpen")
    private val drawers = mutableMapOf<Activity, DebugMenuDrawer>()
    private var configuration = DebugMenuConfiguration()
    private val lifecycleCallbacks = object : SimpleActivityLifecycleCallbacks() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            drawers[activity] = createAndAddDrawerLayout(activity, savedInstanceState?.isDrawerOpen == true)
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            p1.isDrawerOpen = drawers[activity]?.let { drawer -> (drawer.parent as? DebugMenuDrawerLayout?)?.isDrawerOpen(drawer) } ?: false
        }

        override fun onActivityDestroyed(activity: Activity) {
            drawers.remove(activity)
        }
    }

    private fun createAndAddDrawerLayout(activity: Activity, shouldOpenDrawer: Boolean) = DebugMenuDrawer(
        context = activity,
        configuration = configuration
    ).also { drawer ->
        activity.findRootViewGroup().run {
            post {
                val oldViews = (0 until childCount).map { getChildAt(it) }
                removeAllViews()
                addView(
                    DebugMenuDrawerLayout(
                        context = activity,
                        oldViews = oldViews,
                        drawer = drawer,
                        drawerWidth = configuration.drawerWidth
                    ).apply { if (shouldOpenDrawer) openDrawer(drawer) },
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    }

    private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)
    //endregion
}