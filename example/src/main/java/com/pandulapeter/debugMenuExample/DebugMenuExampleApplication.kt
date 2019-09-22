package com.pandulapeter.debugMenuExample

import android.app.Application
import android.widget.Toast
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayModule
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.SettingsLinkModule
import com.pandulapeter.debugMenuCore.configuration.modules.ToggleModule
import com.pandulapeter.debugMenuExample.networking.NetworkingManager
import com.pandulapeter.debugMenuExample.utils.mockBackendEnvironments

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            DebugMenu.attachToUi(this)
            DebugMenu.modules = listOf(
                HeaderModule(
                    title = getString(R.string.app_name),
                    subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    text = "Hello QA person!"
                ),
                SettingsLinkModule(),
                KeylineOverlayModule(),
                ToggleModule(
                    title = "Feature toggle 1",
                    onValueChanged = { isOn -> "Feature 1 is ${if (isOn) "on" else "off"}".showToast() }
                ),
                ToggleModule(
                    title = "Feature toggle 2",
                    onValueChanged = { isOn -> "Feature 2 is ${if (isOn) "on" else "off"}".showToast() }
                ),
                NetworkLoggingModule(
                    baseUrl = NetworkingManager.BASE_URL,
                    shouldShowHeaders = true,
                    shouldShowTimestamp = true
                ),
                LoggingModule(shouldShowTimestamp = true),
                ListModule(
                    title = "Environment",
                    items = mockBackendEnvironments,
                    onItemSelected = { id -> mockBackendEnvironments.firstOrNull { it.id == id }?.also { environment -> environment.url.showToast() } }
                )
            )
        }
    }

    private fun String.showToast() = Toast.makeText(this@DebugMenuExampleApplication, this, Toast.LENGTH_SHORT).show()
}