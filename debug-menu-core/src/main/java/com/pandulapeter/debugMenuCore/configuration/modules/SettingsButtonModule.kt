package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays a button which opens the app's Android Settings page, when pressed.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Open Settings" by default.
 */
data class SettingsButtonModule(
    val text: String = "Open Settings"
) : DebugMenuModule {

    override val id = ID

    companion object {
        const val ID = "settingsButton"
    }
}