package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_SHOULD_OPEN_IN_NEW_TASK
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule.Companion.ID


/**
 * Displays a button that links to the system settings' Developer Options page.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param shouldOpenInNewTask - Whether or not the App Info page will be opened with the Intent.FLAG_ACTIVITY_NEW_TASK flag. [DEFAULT_SHOULD_OPEN_IN_NEW_TASK] by default.
 * @param onButtonPressed - Callback called when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 */
data class DeveloperOptionsButtonModule(
    val text: Text = Text.CharSequence(DEFAULT_TEXT),
    val shouldOpenInNewTask: Boolean = DEFAULT_SHOULD_OPEN_IN_NEW_TASK,
    val onButtonPressed: () -> Unit = DEFAULT_ON_BUTTON_PRESSED
) : Module<DeveloperOptionsButtonModule> {

    constructor(
        text: CharSequence = DEFAULT_TEXT,
        shouldOpenInNewTask: Boolean = DEFAULT_SHOULD_OPEN_IN_NEW_TASK,
        onButtonPressed: () -> Unit = DEFAULT_ON_BUTTON_PRESSED
    ) : this(
        text = Text.CharSequence(text),
        shouldOpenInNewTask = shouldOpenInNewTask,
        onButtonPressed = onButtonPressed
    )

    override val id: String = ID

    companion object {
        const val ID = "developerOptionsButton"
        private const val DEFAULT_TEXT = "Developer options"
        private const val DEFAULT_SHOULD_OPEN_IN_NEW_TASK = false
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
    }
}