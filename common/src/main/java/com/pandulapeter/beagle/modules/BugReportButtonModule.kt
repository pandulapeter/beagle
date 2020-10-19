package com.pandulapeter.beagle.modules

import android.app.Application
import android.net.Uri
import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_BUILD_INFORMATION
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_DESCRIPTION_TEMPLATE
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_ICON
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_LABEL_SECTIONS_TO_SHOW
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_ON_BUG_REPORT_READY
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_SHOULD_SHOW_GALLERY_SECTION
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_SHOULD_SHOW_METADATA_SECTION
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.DEFAULT_TYPE
import com.pandulapeter.beagle.modules.BugReportButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.GalleryButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ScreenshotButtonModule.Companion.ID


/**
 * Displays a button that opens the bug reporting screen. Empty sections will not be displayed.
 * Check out the [Appearance] class for customization options.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param shouldShowGallerySection - Whether or not the gallery section should be added. [DEFAULT_SHOULD_SHOW_GALLERY_SECTION] by default.
 * @param shouldShowNetworkLogsSection - Whether or not the section of network logs should be added. [DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION] by default.
 * @param logLabelSectionsToShow - The list of log tags for which sections should be added. [DEFAULT_LABEL_SECTIONS_TO_SHOW] by default, which adds a section for all logs, without filtering.
 * @param shouldShowMetadataSection - Whether or not the metadata section (build information and device information) should be added. [DEFAULT_SHOULD_SHOW_METADATA_SECTION] by default.
 * @param buildInformation - The list of key-value pairs that should be attached to reports as build information. The library can't figure out many important things so it is recommended to override the default value. [DEFAULT_BUILD_INFORMATION] by default.
 * @param descriptionTemplate - The default value of the free-text input. [DEFAULT_DESCRIPTION_TEMPLATE] by default.
 * @param type - Specify a [TextModule.Type] to apply a specific appearance. [DEFAULT_TYPE] by default.
 * @param icon - A drawable resource ID that will be tinted and displayed before the text, or null to display no icon. [DEFAULT_ICON] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param onButtonPressed - Callback invoked when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 * @param onBugReportReady - The lambda that gets invoked after the bug report is ready, with the [Uri] pointing to the ZIP file, or null for the default implementation that uses the system share sheet. [DEFAULT_ON_BUG_REPORT_READY] by default.
 */
@Suppress("unused")
data class BugReportButtonModule(
    val text: Text = Text.CharSequence(DEFAULT_TEXT),
    val shouldShowGallerySection: Boolean = DEFAULT_SHOULD_SHOW_GALLERY_SECTION,
    val shouldShowNetworkLogsSection: Boolean = DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION,
    val logLabelSectionsToShow: List<String?> = DEFAULT_LABEL_SECTIONS_TO_SHOW,
    val shouldShowMetadataSection: Boolean = DEFAULT_SHOULD_SHOW_METADATA_SECTION,
    val buildInformation: (activity: Application?) -> List<Pair<Text, String>> = DEFAULT_BUILD_INFORMATION,
    val descriptionTemplate: Text = Text.CharSequence(DEFAULT_DESCRIPTION_TEMPLATE),
    val type: TextModule.Type = DEFAULT_TYPE,
    @DrawableRes val icon: Int? = DEFAULT_ICON,
    val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    val onButtonPressed: () -> Unit = {},
    val onBugReportReady: ((bugReport: Uri?) -> Unit)? = DEFAULT_ON_BUG_REPORT_READY
) : Module<BugReportButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "bugReportButton"
        private const val DEFAULT_TEXT = "Report a bug"
        private const val DEFAULT_SHOULD_SHOW_GALLERY_SECTION = true
        private const val DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION = true
        private val DEFAULT_LABEL_SECTIONS_TO_SHOW = listOf<String?>(null)
        private const val DEFAULT_SHOULD_SHOW_METADATA_SECTION = true
        val DEFAULT_BUILD_INFORMATION: (Application?) -> List<Pair<Text, String>> = { application ->
            mutableListOf<Pair<Text, String>>().apply {
                if (application != null) {
                    add(Text.CharSequence("Package name") to application.packageName)
                }
            }
        }
        private const val DEFAULT_DESCRIPTION_TEMPLATE = ""
        private val DEFAULT_TYPE = TextModule.Type.NORMAL
        private val DEFAULT_ICON: Int? = null
        private const val DEFAULT_IS_ENABLED = true
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
        private val DEFAULT_ON_BUG_REPORT_READY: ((Uri?) -> Unit)? = null
    }
}