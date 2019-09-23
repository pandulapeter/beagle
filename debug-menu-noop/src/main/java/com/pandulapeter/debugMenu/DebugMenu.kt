package com.pandulapeter.debugMenu

import android.app.Activity
import android.app.Application
import com.pandulapeter.debugMenuCore.ModulePositioning
import com.pandulapeter.debugMenuCore.configuration.UiCustomization
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuModule
import com.pandulapeter.debugMenuCore.contracts.DebugMenuContract

/**
 * Fake implementation to be used in release builds.
 */
object DebugMenu : DebugMenuContract {

    /**
     * Does nothing.
     */
    override var isEnabled = false
        set(_) = Unit

    /**
     * Does nothing.
     */
    override fun attachToApplication(application: Application, uiCustomization: UiCustomization) = Unit

    /**
     * Does nothing.
     */
    override fun setModules(modules: List<DebugMenuModule>) = Unit

    /**
     * Does nothing.
     */
    override fun putModule(module: DebugMenuModule, positioning: ModulePositioning) = Unit

    /**
     * Does nothing.
     */
    override fun removeModule(id: String) = Unit

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
    override fun log(message: String, tag: String?, payload: String?) = Unit
}