package com.pandulapeter.debugMenuCore

import android.app.Activity
import android.app.Application

/**
 * This interface assures that the real implementation and the "noop" variant have the same API. See one of those for the method documentations.
 */
interface DebugMenu {

    fun initialize(application: Application, configuration: DebugMenuConfiguration)

    fun closeDrawer(activity: Activity): Boolean

    fun openDrawer(activity: Activity)

    fun log(message: String)
}