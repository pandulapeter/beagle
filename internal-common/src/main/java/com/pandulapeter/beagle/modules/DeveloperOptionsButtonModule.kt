package com.pandulapeter.beagle.modules

import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_ICON
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_SHOULD_OPEN_IN_NEW_TASK
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_TYPE
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.ID


/**
 * Displays a button that links to the system settings' Developer Options page.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param shouldOpenInNewTask - Whether or not the App Info page will be opened with the Intent.FLAG_ACTIVITY_NEW_TASK flag. [DEFAULT_SHOULD_OPEN_IN_NEW_TASK] by default.
 * @param type - Specify a [TextModule.Type] to apply a specific appearance. [DEFAULT_TYPE] by default.
 * @param icon - A drawable resource ID that will be tinted and displayed before the text, or null to display no icon. [DEFAULT_ICON] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param onButtonPressed - Callback invoked when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 */
data class DeveloperOptionsButtonModule(
    val text: Text = DEFAULT_TEXT.toText(),
    val shouldOpenInNewTask: Boolean = DEFAULT_SHOULD_OPEN_IN_NEW_TASK,
    val type: TextModule.Type = DEFAULT_TYPE,
    @DrawableRes val icon: Int? = DEFAULT_ICON,
    val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    val onButtonPressed: () -> Unit = DEFAULT_ON_BUTTON_PRESSED
) : Module<DeveloperOptionsButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "developerOptionsButton"
        private const val DEFAULT_TEXT = "Developer options"
        private const val DEFAULT_SHOULD_OPEN_IN_NEW_TASK = false
        private val DEFAULT_TYPE = TextModule.Type.BUTTON
        private val DEFAULT_ICON: Int? = null
        private const val DEFAULT_IS_ENABLED = true
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
    }
}