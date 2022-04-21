package com.pandulapeter.beagle.modules

import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.DEFAULT_ICON
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.DEFAULT_TYPE
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that takes a screen recording video of the current layout and allows the user to share it.
 * A notification will appear during the recording which contains the button to stop it. The recording will have at most 720p resolution.
 * Recording will only be started after the user agrees to the system prompt.
 * Check out the [Behavior] class to override the default file naming logic.
 *
 * The app will show a media preview dialog when the recording is done, or a notification that opens to the gallery if it is no longer in the foreground.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param type - Specify a [TextModule.Type] to apply a specific appearance. [DEFAULT_TYPE] by default.
 * @param icon - A drawable resource ID that will be tinted and displayed before the text, or null to display no icon. [DEFAULT_ICON] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param onButtonPressed - Callback invoked when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 */
@Suppress("unused")
data class ScreenRecordingButtonModule(
    val text: Text = DEFAULT_TEXT.toText(),
    val type: TextModule.Type = DEFAULT_TYPE,
    @DrawableRes val icon: Int? = DEFAULT_ICON,
    val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    val onButtonPressed: () -> Unit = DEFAULT_ON_BUTTON_PRESSED
) : Module<ScreenRecordingButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "screenRecordingButton"
        private const val DEFAULT_TEXT = "Record a video"
        private val DEFAULT_TYPE = TextModule.Type.NORMAL
        private val DEFAULT_ICON: Int? = null
        private const val DEFAULT_IS_ENABLED = true
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
    }
}