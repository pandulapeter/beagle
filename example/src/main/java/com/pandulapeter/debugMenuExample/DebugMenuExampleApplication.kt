package com.pandulapeter.debugMenuExample

import android.app.Application
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.ModuleConfiguration
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayModule
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.SettingsLinkModule

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
                keylineOverlayModule = KeylineOverlayModule(),
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