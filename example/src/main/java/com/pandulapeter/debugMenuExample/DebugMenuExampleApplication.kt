package com.pandulapeter.debugMenuExample

import android.app.Application
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayModule
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.SettingsLinkModule
import com.pandulapeter.debugMenuExample.networking.NetworkingManager

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            DebugMenu.initialize(this)
            DebugMenu.modules = listOf(
                HeaderModule(
                    title = getString(R.string.app_name),
                    subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    text = "Hello QA person!"
                ),
                SettingsLinkModule(),
                KeylineOverlayModule(),
                NetworkLoggingModule(
                    baseUrl = NetworkingManager.BASE_URL,
                    shouldShowHeaders = true,
                    shouldShowTimestamp = true
                ),
                LoggingModule(shouldShowTimestamp = true)
            )
        }
    }
}