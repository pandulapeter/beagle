package com.pandulapeter.beagleCore.configuration.tricks

/**
 * Displays a button that links to the Android App Info page for your app.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Show app info" by default.
 */
data class AppInfoButtonTrick(
    val text: CharSequence = "Show app info"
) : Trick {

    override val id = ID

    companion object {
        const val ID = "appInfoButton"
    }
}

@Suppress("unused")
typealias AppInfoButtonModule = AppInfoButtonTrick