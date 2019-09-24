package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays a button that takes a screenshot of the current layout and allows the user to share it.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Take screenshot by default.
 */
data class ScreenshotButtonModule(
    val text: CharSequence = "Take screenshot"
) : DebugMenuModule {

    override val id = ID

    companion object {
        const val ID = "screenshotButton"
    }
}