package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Functionality-wise this module adds a [ScreenshotButtonModule], a [ScreenRecordingButtonModule] (on Lollipop and above) and a [GalleryButtonModule] to the debug menu.
 * However, they will be displayed as list items. Check out the linked module descriptions for mode information.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module that will be displayed in the header of the list. "Screen capture tools" by default.
 * @param imageText - The text that should be displayed on the screenshot button, or null to completely skip adding this item. "Take a screenshot" by default.
 * @param videoText - The text that should be displayed on the screen recording button, or null to completely skip adding this item. "Record a video" by default.
 * @param galleryText - The text that should be displayed on the gallery button. "Open the gallery" by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 */
data class ScreenCaptureToolboxModule(
    override val title: CharSequence = "Screen capture tools",
    val imageText: CharSequence? = "Take a screenshot",
    val videoText: CharSequence? = "Record a video",
    val galleryText: CharSequence = "Open the gallery",
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<ScreenCaptureToolboxModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "screenCaptureToolbox"
    }
}