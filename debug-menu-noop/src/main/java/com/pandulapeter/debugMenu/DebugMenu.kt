package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import com.pandulapeter.debugMenuCore.DebugMenuContract
import com.pandulapeter.debugMenuCore.ModuleConfiguration
import com.pandulapeter.debugMenuCore.UiConfiguration

/**
 * Fake implementation to be used in release builds.
 */
object DebugMenu : DebugMenuContract {

    /**
     * Does nothing.
     */
    override var moduleConfiguration = ModuleConfiguration()

    /**
     * Does nothing.
     */
    override fun initialize(application: Application, uiConfiguration: UiConfiguration, moduleConfiguration: ModuleConfiguration) = Unit

    /**
     * Does nothing and returns false.
     */
    override fun closeDrawer(activity: Activity) = false

    /**
     * Does nothing.
     */
    override fun openDrawer(activity: Activity) = Unit

    /**
     * Does nothing.
     */
    override fun log(message: String) = Unit
}