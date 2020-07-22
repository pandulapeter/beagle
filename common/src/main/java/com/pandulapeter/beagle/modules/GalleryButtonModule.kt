package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.GalleryButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that opens the gallery of captured screenshot images and screen recording videos.
 * Check out the [Appearance] class for customization options.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. "Open the gallery" by default.
 * @param onButtonPressed - Callback called when the user presses the button. Optional, empty implementation by default.
 */
data class GalleryButtonModule(
    val text: CharSequence = "Open the gallery",
    val onButtonPressed: () -> Unit = {}
) : Module<GalleryButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "galleryButton"
    }
}