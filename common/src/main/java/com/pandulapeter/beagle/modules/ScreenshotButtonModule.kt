package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that takes a screenshot of the current layout and allows the user to share it.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. "Take a screenshot" by default.
 * @param onButtonPressed - Callback called when the user presses the button. Optional, empty implementation by default.
 */
data class ScreenshotButtonModule(
    val text: CharSequence = "Take a screenshot",
    val onButtonPressed: () -> Unit = {}
) : Module<ScreenshotButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "screenshotButton"
    }
}