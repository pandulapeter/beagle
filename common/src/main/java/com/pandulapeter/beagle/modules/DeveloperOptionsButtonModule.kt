package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID


/**
 * Displays a button that links to the system settings' Developer Options page.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. Optional, "Developer options" by default.
 * @param shouldOpenInNewTask - Whether or not the App Info page will be opened with the Intent.FLAG_ACTIVITY_NEW_TASK flag. False by default.
 * @param onButtonPressed - Callback called when the user presses the button. Optional, empty implementation by default.
 */
data class DeveloperOptionsButtonModule(
    val text: CharSequence = "Developer options",
    val shouldOpenInNewTask: Boolean = false,
    val onButtonPressed: () -> Unit = {}
) : Module<DeveloperOptionsButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "developerOptionsButton"
    }
}