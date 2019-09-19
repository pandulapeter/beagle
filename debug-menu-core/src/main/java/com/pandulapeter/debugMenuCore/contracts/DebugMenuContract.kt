package com.pandulapeter.debugMenuCore.contracts

import android.app.Activity
import android.app.Application
import com.pandulapeter.debugMenuCore.configuration.ModuleConfiguration
import com.pandulapeter.debugMenuCore.configuration.UiConfiguration

/**
 * This interface assures that the real implementation and the "noop" variant have the same API. See one of those for the method documentations.
 */
interface DebugMenuContract {

    var moduleConfiguration: ModuleConfiguration

    fun initialize(application: Application, uiConfiguration: UiConfiguration = UiConfiguration(), moduleConfiguration: ModuleConfiguration = ModuleConfiguration())

    fun closeDrawer(activity: Activity): Boolean

    fun openDrawer(activity: Activity)

    fun log(message: String, tag: String? = null, payload: String? = null)
}