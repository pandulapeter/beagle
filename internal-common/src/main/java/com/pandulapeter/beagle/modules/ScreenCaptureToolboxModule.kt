package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.DEFAULT_GALLERY_TEXT
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.DEFAULT_IMAGE_TEXT
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.DEFAULT_TITLE
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.DEFAULT_VIDEO_TEXT
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Functionality-wise this module adds a [ScreenshotButtonModule], a [ScreenRecordingButtonModule] and a [GalleryButtonModule] to the debug menu.
 * However, they will be displayed as list items. Check out the linked module descriptions for mode information.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module that will be displayed in the header of the list. [DEFAULT_TITLE] by default.
 * @param imageText - The text that should be displayed on the screenshot button, or null to completely skip adding this item. [DEFAULT_IMAGE_TEXT] by default.
 * @param videoText - The text that should be displayed on the screen recording button, or null to completely skip adding this item. [DEFAULT_VIDEO_TEXT] by default.
 * @param galleryText - The text that should be displayed on the gallery button. [DEFAULT_GALLERY_TEXT] by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 */
@Suppress("unused")
data class ScreenCaptureToolboxModule(
    val title: Text = DEFAULT_TITLE.toText(),
    val imageText: Text? = DEFAULT_IMAGE_TEXT.toText(),
    val videoText: Text? = DEFAULT_VIDEO_TEXT.toText(),
    val galleryText: Text = DEFAULT_GALLERY_TEXT.toText(),
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY
) : ExpandableModule<ScreenCaptureToolboxModule> {

    override val id: String = ID

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        const val ID = "screenCaptureToolbox"
        private const val DEFAULT_TITLE = "Screen capture tools"
        private const val DEFAULT_IMAGE_TEXT = "Take a screenshot"
        private const val DEFAULT_VIDEO_TEXT = "Record a video"
        private const val DEFAULT_GALLERY_TEXT = "Open the gallery"
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}