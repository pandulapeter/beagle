package com.pandulapeter.beagleExample

import android.app.Application
import android.widget.Toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.modules.AppInfoButtonModule
import com.pandulapeter.beagleCore.configuration.modules.ButtonModule
import com.pandulapeter.beagleCore.configuration.modules.HeaderModule
import com.pandulapeter.beagleCore.configuration.modules.KeylineOverlayToggleModule
import com.pandulapeter.beagleCore.configuration.modules.LogListModule
import com.pandulapeter.beagleCore.configuration.modules.LongTextModule
import com.pandulapeter.beagleCore.configuration.modules.NetworkLogListModule
import com.pandulapeter.beagleCore.configuration.modules.ScreenshotButtonModule
import com.pandulapeter.beagleCore.configuration.modules.SingleSelectionListModule
import com.pandulapeter.beagleCore.configuration.modules.TextModule
import com.pandulapeter.beagleCore.configuration.modules.ToggleModule
import com.pandulapeter.beagleExample.networking.NetworkingManager
import com.pandulapeter.beagleExample.utils.mockBackendEnvironments

@Suppress("unused")
class BeagleExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Beagle.imprint(
                application = this,
                appearance = Appearance(themeResourceId = R.style.BeagleTheme)
            )
            Beagle.learn(
                listOf(
                    HeaderModule(
                        title = getString(R.string.app_name),
                        subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        text = "Built on ${BuildConfig.BUILD_DATE}"
                    ),
                    AppInfoButtonModule(),
                    ScreenshotButtonModule(),
                    KeylineOverlayToggleModule(),
                    ToggleModule(
                        title = "Feature toggle 1",
                        onValueChanged = { isOn -> "Feature 1 is ${if (isOn) "on" else "off"}".showToast() }
                    ),
                    ToggleModule(
                        title = "Feature toggle 2",
                        onValueChanged = { isOn -> "Feature 2 is ${if (isOn) "on" else "off"}".showToast() }
                    ),
                    NetworkLogListModule(
                        baseUrl = NetworkingManager.BASE_URL,
                        shouldShowHeaders = true,
                        shouldShowTimestamp = true
                    ),
                    LogListModule(shouldShowTimestamp = true),
                    SingleSelectionListModule(
                        title = "Environment",
                        items = mockBackendEnvironments,
                        isInitiallyExpanded = true,
                        initialSelectionId = "Develop",
                        onItemSelectionChanged = { backendEnvironment -> backendEnvironment.url.showToast() }
                    ),
                    TextModule(
                        text = "This is a TextModule used for displaying short hints"
                    ),
                    LongTextModule(
                        title = "Long text",
                        text = "Here is a longer piece of text that occupies more space so it doesn't make sense to always have it fully displayed."
                    ),
                    ButtonModule(
                        text = "Show a toast",
                        onButtonPressed = { "Here is a toast".showToast() }
                    )
                )
            )
        }
    }

    private fun String.showToast() = Toast.makeText(this@BeagleExampleApplication, this, Toast.LENGTH_SHORT).show()
}