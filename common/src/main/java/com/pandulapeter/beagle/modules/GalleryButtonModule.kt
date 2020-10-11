package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.GalleryButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.GalleryButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.GalleryButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that opens the gallery of captured screenshot images and screen recording videos.
 * Check out the [Appearance] class for customization options.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param onButtonPressed - Callback called when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 */
data class GalleryButtonModule(
    val text: CharSequence = DEFAULT_TEXT,
    val onButtonPressed: () -> Unit = {}
) : Module<GalleryButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "galleryButton"
        private const val DEFAULT_TEXT = "Open the gallery"
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
    }
}