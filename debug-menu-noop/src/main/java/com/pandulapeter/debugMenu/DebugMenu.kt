package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import com.pandulapeter.debugMenuCore.DebugMenu
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration

/**
 * Fake implementation to be used in release builds.
 */
object DebugMenu : DebugMenu {

    /**
     * Does nothing.
     */
    override fun initialize(application: Application, configuration: DebugMenuConfiguration) = Unit


    /**
     * Does nothing and returns fale.
     */
    override fun closeDrawer(activity: Activity) = false


    /**
     * Does nothing.
     */
    override fun openDrawer(activity: Activity) = Unit
}