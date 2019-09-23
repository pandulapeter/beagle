package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays a button which opens the app's Android App Info page, when pressed.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Show app info" by default.
 */
data class AppInfoButtonModule(
    val text: CharSequence = "Show app info"
) : DebugMenuModule {

    override val id = ID

    companion object {
        const val ID = "appInfoButton"
    }
}