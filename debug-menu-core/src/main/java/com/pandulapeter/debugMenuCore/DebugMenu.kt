package com.pandulapeter.debugMenuCore

import android.app.Activity
import android.app.Application

interface DebugMenu {

    fun initialize(application: Application, configuration: DebugMenuConfiguration)

    fun closeDrawerIfOpen(activity: Activity): Boolean
}