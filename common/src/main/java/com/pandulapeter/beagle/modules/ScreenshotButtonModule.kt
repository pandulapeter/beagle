package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that takes a screenshot image of the current layout.
 * Below Android Lollipop the root view's drawing cache will be used which is an inferior solution (only the current decorView will be captured, without system decorations).
 * Above Android Lollipop the entire screen will be captured, after the user agrees to the system prompt.
 * Check out the [Behavior] class to override the default file naming logic.
 *
 * The app will show a media preview dialog when the recording is done, or a notification that opens to the gallery if it is no longer in the foreground.
 *
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