package com.pandulapeter.beagleExample

import android.app.Application
import android.widget.Toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.tricks.AppInfoButtonTrick
import com.pandulapeter.beagleCore.configuration.tricks.ButtonTrick
import com.pandulapeter.beagleCore.configuration.tricks.HeaderTrick
import com.pandulapeter.beagleCore.configuration.tricks.KeylineOverlayToggleTrick
import com.pandulapeter.beagleCore.configuration.tricks.LogListTrick
import com.pandulapeter.beagleCore.configuration.tricks.LongTextTrick
import com.pandulapeter.beagleCore.configuration.tricks.NetworkLogListTrick
import com.pandulapeter.beagleCore.configuration.tricks.ScreenshotButtonTrick
import com.pandulapeter.beagleCore.configuration.tricks.SingleSelectionListTrick
import com.pandulapeter.beagleCore.configuration.tricks.TextTrick
import com.pandulapeter.beagleCore.configuration.tricks.ToggleTrick
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
                    HeaderTrick(
                        title = getString(R.string.app_name),
                        subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        text = "Built on ${BuildConfig.BUILD_DATE}"
                    ),
                    AppInfoButtonTrick(),
                    ScreenshotButtonTrick(),
                    KeylineOverlayToggleTrick(),
                    ToggleTrick(
                        title = "Feature toggle 1",
                        onValueChanged = { isOn -> "Feature 1 is ${if (isOn) "on" else "off"}".showToast() }
                    ),
                    ToggleTrick(
                        title = "Feature toggle 2",
                        onValueChanged = { isOn -> "Feature 2 is ${if (isOn) "on" else "off"}".showToast() }
                    ),
                    NetworkLogListTrick(
                        baseUrl = NetworkingManager.BASE_URL,
                        shouldShowHeaders = true,
                        shouldShowTimestamp = true
                    ),
                    LogListTrick(shouldShowTimestamp = true),
                    SingleSelectionListTrick(
                        title = "Environment",
                        items = mockBackendEnvironments,
                        isInitiallyExpanded = true,
                        initialSelectionId = "Develop",
                        onItemSelectionChanged = { backendEnvironment -> backendEnvironment.url.showToast() }
                    ),
                    TextTrick(
                        text = "This is a TextTrick used for displaying short hints"
                    ),
                    LongTextTrick(
                        title = "Long text",
                        text = "Here is a longer piece of text that occupies more space so it doesn't make sense to always have it fully displayed."
                    ),
                    ButtonTrick(
                        text = "Show a toast",
                        onButtonPressed = { "Here is a toast".showToast() }
                    )
                )
            )
        }
    }

    private fun String.showToast() = Toast.makeText(this@BeagleExampleApplication, this, Toast.LENGTH_SHORT).show()
}