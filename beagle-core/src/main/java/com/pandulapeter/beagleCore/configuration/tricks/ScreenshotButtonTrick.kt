package com.pandulapeter.beagleCore.configuration.tricks

/**
 * Displays a button that takes a screenshot of the current layout and allows the user to share it.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Take a screenshot" by default.
 */
data class ScreenshotButtonTrick(
    val text: CharSequence = "Take a screenshot"
) : Trick {

    override val id = ID

    companion object {
        const val ID = "screenshotButton"
    }
}

@Suppress("unused")
typealias ScreenshotButtonModule = ScreenshotButtonTrick