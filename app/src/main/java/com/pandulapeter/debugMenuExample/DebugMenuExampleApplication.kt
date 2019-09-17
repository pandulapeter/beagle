package com.pandulapeter.debugMenuExample

import android.app.Application
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugMenu.initialize(
            application = this,
            configuration = DebugMenuConfiguration(
                title = getString(R.string.app_name),
                version = BuildConfig.VERSION_NAME
            )
        )
    }
}