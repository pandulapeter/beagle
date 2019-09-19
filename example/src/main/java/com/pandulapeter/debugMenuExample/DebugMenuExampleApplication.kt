package com.pandulapeter.debugMenuExample

import android.app.Application
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.ModuleConfiguration
import com.pandulapeter.debugMenuCore.modules.HeaderModule
import com.pandulapeter.debugMenuCore.modules.LoggingModule
import com.pandulapeter.debugMenuCore.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.modules.SettingsLinkModule

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugMenu.initialize(
            application = this,
            moduleConfiguration = ModuleConfiguration(
                headerModule = HeaderModule(
                    title = getString(R.string.app_name),
                    subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    text = "Hello QA person!"
                ),
                settingsLinkModule = SettingsLinkModule(),
                networkLoggingModule = NetworkLoggingModule(
                    shouldShowTimestamp = true
                ),
                loggingModule = LoggingModule(
                    shouldShowTimestamp = true
                )
            )
        )
    }
}