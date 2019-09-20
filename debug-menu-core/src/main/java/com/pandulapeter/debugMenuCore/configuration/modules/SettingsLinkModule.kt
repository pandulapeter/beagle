package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays a button which opens the app's Android Settings page, when pressed.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Open Settings" by default.
 */
//TODO: Create a more generic parent module for buttons.
data class SettingsLinkModule(
    val text: String = "Open Settings"
) : DebugMenuModule {

    override val id = "settingsLink"
}