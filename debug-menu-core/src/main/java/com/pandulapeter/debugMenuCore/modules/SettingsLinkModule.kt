package com.pandulapeter.debugMenuCore.modules

/**
 * Displays a button which opens the app's Android Settings page, when pressed.
 * @param text - The text that should be displayed on the button. "Open Settings" by default.
 */
data class SettingsLinkModule(
    val text: String = "Open Settings"
) : DebugMenuModule