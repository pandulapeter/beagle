package com.pandulapeter.debugMenuExample

import android.app.Application
import android.graphics.Color
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration
import com.pandulapeter.debugMenuCore.modules.HeaderModule

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugMenu.initialize(
            application = this,
            configuration = DebugMenuConfiguration(
                backgroundColor = Color.BLACK,
                textColor = Color.WHITE,
                modules = listOf(
                    HeaderModule(
                        title = getString(R.string.app_name),
                        subtitle = BuildConfig.VERSION_NAME,
                        shouldShowBuildTime = true
                    )
                )
            )
        )
    }
}