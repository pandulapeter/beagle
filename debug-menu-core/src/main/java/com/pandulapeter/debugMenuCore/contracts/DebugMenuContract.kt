package com.pandulapeter.debugMenuCore.contracts

import android.app.Activity
import android.app.Application
import com.pandulapeter.debugMenuCore.ModulePositioning
import com.pandulapeter.debugMenuCore.configuration.UiCustomization
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuModule

/**
 * This interface assures that the real implementation and the "noop" variant have the same API. See one of those for the method documentations.
 */
interface DebugMenuContract {

    var isEnabled: Boolean

    fun attachToApplication(application: Application, uiCustomization: UiCustomization = UiCustomization())

    fun setModules(modules: List<DebugMenuModule>)

    fun putModule(module: DebugMenuModule, positioning: ModulePositioning = ModulePositioning.Bottom)

    fun removeModule(id: String)

    fun closeDrawer(activity: Activity): Boolean

    fun openDrawer(activity: Activity)

    fun log(message: String, tag: String? = null, payload: String? = null)
}