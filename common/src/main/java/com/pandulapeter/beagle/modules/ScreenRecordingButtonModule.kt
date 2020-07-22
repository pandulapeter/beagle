package com.pandulapeter.beagle.modules

import android.os.Build
import androidx.annotation.RequiresApi
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that takes a screen recording video of the current layout and allows the user to share it.
 * A notification will appear during the recording which contains the button to stop it. The recording will have at most 720p resolution.
 * This feature relies on API-s only present on Android Lollipop and above. Recording will only be started after the user agrees to the system prompt.
 * Check out the [Behavior] class to override the default file naming logic.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. "Record a video" by default.
 * @param onButtonPressed - Callback called when the user presses the button. Optional, empty implementation by default.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
data class ScreenRecordingButtonModule(
    val text: CharSequence = "Record a video",
    val onButtonPressed: () -> Unit = {}
) : Module<ScreenRecordingButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "screenRecordingButton"
    }
}