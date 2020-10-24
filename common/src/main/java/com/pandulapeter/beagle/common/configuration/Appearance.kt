package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StyleRes
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_BUILD_INFORMATION
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_DEVICE_INFORMATION
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_GALLERY_SECTION_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_LOGS_SECTION_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_METADATA_SECTION_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_NETWORK_LOGS_SECTION_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_SEND_BUTTON_HINT
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_SHOW_MORE_TEXT
import com.pandulapeter.beagle.common.configuration.Appearance.BugReportTexts.Companion.DEFAULT_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.Companion.DEFAULT_APPLY_INSETS
import com.pandulapeter.beagle.common.configuration.Appearance.Companion.DEFAULT_THEME_RESOURCE_ID
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts.Companion.DEFAULT_ANDROID_VERSION
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts.Companion.DEFAULT_DENSITY
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts.Companion.DEFAULT_MANUFACTURER
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts.Companion.DEFAULT_MODEL
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts.Companion.DEFAULT_RESOLUTION_DP
import com.pandulapeter.beagle.common.configuration.Appearance.DeviceInfoTexts.Companion.DEFAULT_RESOLUTION_PX
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_DELETE_CONFIRMATION_MESSAGE_PLURAL
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_DELETE_CONFIRMATION_MESSAGE_SINGULAR
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_DELETE_CONFIRMATION_NEGATIVE
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_DELETE_CONFIRMATION_POSITIVE
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_DELETE_HINT
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_NO_MEDIA_MESSAGE
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts.Companion.DEFAULT_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.GeneralTexts
import com.pandulapeter.beagle.common.configuration.Appearance.GeneralTexts.Companion.DEFAULT_APPLY_BUTTON_TEXT
import com.pandulapeter.beagle.common.configuration.Appearance.GeneralTexts.Companion.DEFAULT_RESET_BUTTON_TEXT
import com.pandulapeter.beagle.common.configuration.Appearance.GeneralTexts.Companion.DEFAULT_SHARE_HINT
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts.Companion.DEFAULT_DURATION
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts.Companion.DEFAULT_HEADERS
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts.Companion.DEFAULT_METADATA
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts.Companion.DEFAULT_NONE
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts.Companion.DEFAULT_TIMESTAMP
import com.pandulapeter.beagle.common.configuration.Appearance.NetworkLogTexts.Companion.DEFAULT_TOGGLE_EXPAND_COLLAPSE
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts.Companion.DEFAULT_IN_PROGRESS_NOTIFICATION_CONTENT
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts.Companion.DEFAULT_IN_PROGRESS_NOTIFICATION_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts.Companion.DEFAULT_NOTIFICATION_CHANNEL_NAME
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts.Companion.DEFAULT_READY_NOTIFICATION_CONTENT
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts.Companion.DEFAULT_READY_NOTIFICATION_TITLE
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts.Companion.DEFAULT_TOAST_TEXT
import com.pandulapeter.beagle.commonBase.GALLERY_DATE_FORMAT
import com.pandulapeter.beagle.commonBase.LOG_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the appearance customization options for the debug menu. Used as an optional argument of Beagle.initialize(). All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID to be used for the debug menu as well as the Gallery screen. If null, the current Activity's theme will be used. [DEFAULT_THEME_RESOURCE_ID] by default.
 * @param generalTexts - Customize general UI strings, see [GeneralTexts].
 * @param screenCaptureTexts - Customize UI strings related to screen capture, see [ScreenCaptureTexts].
 * @param galleryTexts - Customize UI strings related to the Gallery, see [GalleryTexts].
 * @param bugReportTexts - Customize UI strings related to the bug reporting screen, see [BugReportTexts].
 * @param networkLogTexts - Customize UI strings related to the network event detail dialog, see [NetworkLogTexts].
 * @param deviceInfoTexts - Customize UI strings related to the device info module (also used when reporting bugs), see [DeviceInfoTexts].
 * @param networkLogTimestampFormatter - The formatter used for displaying the timestamp of network events on the detail dialog. Formats with [LOG_TIME_FORMAT] by default.
 * @param logTimestampFormatter - The formatter used for displaying the timestamp of logs on the detail dialog. Formats with [LOG_TIME_FORMAT] by default.
 * @param galleryTimestampFormatter - The formatter used for displaying the timestamp of each day section in the gallery, or null if the sections should not be displayed at all. Formats with [GALLERY_DATE_FORMAT] by default.
 * @param applyInsets - The library tries to handle window insets the best it can, but this might not work with your specific setup. To override the default behavior, provide a lambda that returns a new [Insets] object. [DEFAULT_APPLY_INSETS] by default.
 */
data class Appearance(
    @StyleRes val themeResourceId: Int? = DEFAULT_THEME_RESOURCE_ID,
    val generalTexts: GeneralTexts = GeneralTexts(),
    val screenCaptureTexts: ScreenCaptureTexts = ScreenCaptureTexts(),
    val galleryTexts: GalleryTexts = GalleryTexts(),
    val bugReportTexts: BugReportTexts = BugReportTexts(),
    val networkLogTexts: NetworkLogTexts = NetworkLogTexts(),
    val deviceInfoTexts: DeviceInfoTexts = DeviceInfoTexts(),
    val networkLogTimestampFormatter: (timestamp: Long) -> CharSequence = { DEFAULT_NETWORK_LOG_DATE_FORMAT.format(it) },
    val logTimestampFormatter: (timestamp: Long) -> CharSequence = { DEFAULT_LOG_DATE_FORMAT.format(it) },
    val galleryTimestampFormatter: ((timestamp: Long) -> CharSequence)? = { DEFAULT_GALLERY_DATE_FORMAT.format(it) },
    val applyInsets: ((windowInsets: Insets) -> Insets)? = DEFAULT_APPLY_INSETS
) {

    /**
     * Holder for general purpose copies used in the debug menu.
     *
     * @param applyButtonText - The text on the Apply button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). [DEFAULT_APPLY_BUTTON_TEXT] by default.
     * @param resetButtonText - The text on the Reset button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). [DEFAULT_RESET_BUTTON_TEXT] by default.
     * @param shareHint - The hint used for the Share icon of the Gallery screen and the Log detail dialog. [DEFAULT_SHARE_HINT] by default.
     */
    data class GeneralTexts(
        val applyButtonText: Text = DEFAULT_APPLY_BUTTON_TEXT.toText(),
        val resetButtonText: Text = DEFAULT_RESET_BUTTON_TEXT.toText(),
        val shareHint: Text = DEFAULT_SHARE_HINT.toText()
    ) {
        companion object {
            private const val DEFAULT_APPLY_BUTTON_TEXT = "Apply"
            private const val DEFAULT_RESET_BUTTON_TEXT = "Reset"
            private const val DEFAULT_SHARE_HINT = "Share"
        }
    }

    /**
     * Holder for copies related to screen capture.
     *
     * @param toastText - A Toast message displayed every time a screen recording is started, or null for no Toast. [DEFAULT_TOAST_TEXT] by default.
     * @param inProgressNotificationTitle - Recording the screen for screenshot images or videos requires a foreground service with a notification. The title for the notification, [DEFAULT_IN_PROGRESS_NOTIFICATION_TITLE] by default.
     * @param inProgressNotificationContent - The content for the notification described for the [inProgressNotificationTitle] parameter. [DEFAULT_IN_PROGRESS_NOTIFICATION_CONTENT] by default.
     * @param readyNotificationTitle - The title for the notification displayed after a screen capture is done, [DEFAULT_READY_NOTIFICATION_TITLE] by default.
     * @param readyNotificationContent - The content for the notification described for the [readyNotificationTitle] parameter. [DEFAULT_READY_NOTIFICATION_CONTENT] by default.
     * @param notificationChannelName - The name for the notification channel that handles all notifications related to screen capture. [DEFAULT_NOTIFICATION_CHANNEL_NAME] by default.
     */
    data class ScreenCaptureTexts(
        val toastText: Text? = DEFAULT_TOAST_TEXT.toText(),
        val inProgressNotificationTitle: Text = DEFAULT_IN_PROGRESS_NOTIFICATION_TITLE.toText(),
        val inProgressNotificationContent: Text = DEFAULT_IN_PROGRESS_NOTIFICATION_CONTENT.toText(),
        val readyNotificationTitle: Text = DEFAULT_READY_NOTIFICATION_TITLE.toText(),
        val readyNotificationContent: Text = DEFAULT_READY_NOTIFICATION_CONTENT.toText(),
        val notificationChannelName: Text = DEFAULT_NOTIFICATION_CHANNEL_NAME.toText()
    ) {
        companion object {
            private const val DEFAULT_TOAST_TEXT = "Recording in progress. Tap on the notification to stop it."
            private const val DEFAULT_IN_PROGRESS_NOTIFICATION_TITLE = "Recording…"
            private const val DEFAULT_IN_PROGRESS_NOTIFICATION_CONTENT = "Tap on this notification when done."
            private const val DEFAULT_READY_NOTIFICATION_TITLE = "Screen captured"
            private const val DEFAULT_READY_NOTIFICATION_CONTENT = "Tap on this notification to open the Gallery."
            private const val DEFAULT_NOTIFICATION_CHANNEL_NAME = "Screen capture notifications"
        }
    }

    /**
     * Holder for copies related to the Gallery screen.
     *
     * @param title - The title of the Gallery screen. [DEFAULT_TITLE] by default.
     * @param noMediaMessage - The empty state text of the Gallery screen. [DEFAULT_NO_MEDIA_MESSAGE] by default.
     * @param deleteHint - The hint used for the Delete icon of the Gallery screen. [DEFAULT_DELETE_HINT] by default.
     * @param deleteConfirmationMessageSingular - The message used for the Delete confirmation dialog of the Gallery screen when deleting one file. [DEFAULT_DELETE_CONFIRMATION_MESSAGE_SINGULAR] by default.
     * @param deleteConfirmationMessagePlural - The message used for the Delete confirmation dialog of the Gallery screen when deleting multiple files. [DEFAULT_DELETE_CONFIRMATION_MESSAGE_PLURAL] by default.
     * @param deleteConfirmationPositive - The positive text used for the Delete confirmation dialog of the Gallery screen. [DEFAULT_DELETE_CONFIRMATION_POSITIVE] by default.
     * @param deleteConfirmationNegative - The positive text used for the Delete confirmation dialog of the Gallery screen. [DEFAULT_DELETE_CONFIRMATION_NEGATIVE] by default.
     */
    data class GalleryTexts(
        val title: Text = DEFAULT_TITLE.toText(),
        val noMediaMessage: Text = DEFAULT_NO_MEDIA_MESSAGE.toText(),
        val deleteHint: Text = DEFAULT_DELETE_HINT.toText(),
        val deleteConfirmationMessageSingular: Text = DEFAULT_DELETE_CONFIRMATION_MESSAGE_SINGULAR.toText(),
        val deleteConfirmationMessagePlural: Text = DEFAULT_DELETE_CONFIRMATION_MESSAGE_PLURAL.toText(),
        val deleteConfirmationPositive: Text = DEFAULT_DELETE_CONFIRMATION_POSITIVE.toText(),
        val deleteConfirmationNegative: Text = DEFAULT_DELETE_CONFIRMATION_NEGATIVE.toText()
    ) {
        companion object {
            private const val DEFAULT_TITLE = "Gallery"
            private const val DEFAULT_NO_MEDIA_MESSAGE = "No media found"
            private const val DEFAULT_DELETE_HINT = "Delete"
            private const val DEFAULT_DELETE_CONFIRMATION_MESSAGE_SINGULAR = "Are you sure you want to delete this file?"
            private const val DEFAULT_DELETE_CONFIRMATION_MESSAGE_PLURAL = "Are you sure you want to delete these files?"
            private const val DEFAULT_DELETE_CONFIRMATION_POSITIVE = "Delete"
            private const val DEFAULT_DELETE_CONFIRMATION_NEGATIVE = "Cancel"
        }
    }

    /**
     * Holder for copies related to the bug reporting screen.
     *
     * @param title - The title of the bug reporting screen. [DEFAULT_TITLE] by default.
     * @param sendButtonHint - The hint that appears on the Send button. [DEFAULT_SEND_BUTTON_HINT] by default.
     * @param gallerySectionTitle - The title of the Gallery section. [DEFAULT_GALLERY_SECTION_TITLE] by default.
     * @param networkLogsSectionTitle - The title of the Network logs section. [DEFAULT_NETWORK_LOGS_SECTION_TITLE] by default.
     * @param logsSectionTitle - The title of the Logs section. Multiple such sections can be added filtered by tags (the parameter of the lambda function). [DEFAULT_LOGS_SECTION_TITLE] by default, suffixed with the tag name if it is not null.
     * @param metadataSectionTitle - The title of the Metadata section. [DEFAULT_METADATA_SECTION_TITLE] by default.
     * @param buildInformation - The text on the build information check box. [DEFAULT_BUILD_INFORMATION] by default.
     * @param deviceInformation - The text on the device information check box. [DEFAULT_DEVICE_INFORMATION] by default.
     * @param showMoreText - The text that appears below lists that have more content. [DEFAULT_SHOW_MORE_TEXT] by default.
     */
    data class BugReportTexts(
        val title: Text = DEFAULT_TITLE.toText(),
        val sendButtonHint: Text = DEFAULT_SEND_BUTTON_HINT.toText(),
        val gallerySectionTitle: (selectedItemCount: Int) -> Text = { selectedItemCount -> "$DEFAULT_GALLERY_SECTION_TITLE ($selectedItemCount $SELECTED)".toText() },
        val networkLogsSectionTitle: (selectedItemCount: Int) -> Text = { selectedItemCount -> "$DEFAULT_NETWORK_LOGS_SECTION_TITLE ($selectedItemCount $SELECTED)".toText() },
        val logsSectionTitle: (tag: String?, selectedItemCount: Int) -> Text = { tag, selectedItemCount -> "${DEFAULT_LOGS_SECTION_TITLE.let { title -> if (tag != null) "$title: $tag" else title }} ($selectedItemCount $SELECTED)".toText() },
        val metadataSectionTitle: Text = DEFAULT_METADATA_SECTION_TITLE.toText(),
        val buildInformation: Text = DEFAULT_BUILD_INFORMATION.toText(),
        val deviceInformation: Text = DEFAULT_DEVICE_INFORMATION.toText(),
        val showMoreText: Text = DEFAULT_SHOW_MORE_TEXT.toText()
    ) {
        companion object {
            private const val DEFAULT_TITLE = "Report a bug"
            private const val DEFAULT_SEND_BUTTON_HINT = "Send bug report"
            private const val DEFAULT_GALLERY_SECTION_TITLE = "Attach media items from the Gallery"
            private const val DEFAULT_NETWORK_LOGS_SECTION_TITLE = "Attach network logs"
            private const val DEFAULT_LOGS_SECTION_TITLE = "Attach logs"
            private const val DEFAULT_METADATA_SECTION_TITLE = "Attach additional details"
            private const val DEFAULT_BUILD_INFORMATION = "Build information"
            private const val DEFAULT_DEVICE_INFORMATION = "Device information"
            private const val DEFAULT_SHOW_MORE_TEXT = "Show more…"
            private const val SELECTED = "selected"
        }
    }

    /**
     * Holder for copies related to the network event detail dialog.
     *
     * @param metadata - The term used for the request / response metadata. [DEFAULT_METADATA] by default.
     * @param headers - The term used for request / response headers. [DEFAULT_HEADERS] by default.
     * @param none - The term used when the list of headers is empty. [DEFAULT_NONE] by default.
     * @param timestamp - The term used for the timestamp of the logged event. [DEFAULT_TIMESTAMP] by default.
     * @param duration - The term used for the duration of the logged event. [DEFAULT_DURATION] by default.
     * @param toggleExpandCollapseHint - The hint for the toggle that expands or collapses all JSON tags. [DEFAULT_TOGGLE_EXPAND_COLLAPSE] by default.
     */
    data class NetworkLogTexts(
        val metadata: Text = DEFAULT_METADATA.toText(),
        val headers: Text = DEFAULT_HEADERS.toText(),
        val none: Text = DEFAULT_NONE.toText(),
        val timestamp: Text = DEFAULT_TIMESTAMP.toText(),
        val duration: Text = DEFAULT_DURATION.toText(),
        val toggleExpandCollapseHint: Text = DEFAULT_TOGGLE_EXPAND_COLLAPSE.toText()
    ) {
        companion object {
            private const val DEFAULT_METADATA = "Metadata"
            private const val DEFAULT_HEADERS = "Headers"
            private const val DEFAULT_NONE = "none"
            private const val DEFAULT_TIMESTAMP = "Timestamp"
            private const val DEFAULT_DURATION = "Duration"
            private const val DEFAULT_TOGGLE_EXPAND_COLLAPSE = "Expand / collapse all"
        }
    }

    /**
     * Holder for copies related to the device info module (also used when reporting bugs).
     *
     * @param manufacturer - The term used for the device manufacturer. [DEFAULT_MANUFACTURER] by default.
     * @param model - The term used for the device model. [DEFAULT_MODEL] by default.
     * @param resolutionPx - The term used for the screen resolution in pixels. [DEFAULT_RESOLUTION_PX] by default.
     * @param resolutionDp - The term used for the screen resolution in dps. [DEFAULT_RESOLUTION_DP] by default.
     * @param density - The term used for the screen density. [DEFAULT_DENSITY] by default.
     * @param androidVersion - The term used for the Android OS version code. [DEFAULT_ANDROID_VERSION] by default.
     */
    data class DeviceInfoTexts(
        val manufacturer: Text = DEFAULT_MANUFACTURER.toText(),
        val model: Text = DEFAULT_MODEL.toText(),
        val resolutionPx: Text = DEFAULT_RESOLUTION_PX.toText(),
        val resolutionDp: Text = DEFAULT_RESOLUTION_DP.toText(),
        val density: Text = DEFAULT_DENSITY.toText(),
        val androidVersion: Text = DEFAULT_ANDROID_VERSION.toText()
    ) {
        companion object {
            private const val DEFAULT_MANUFACTURER = "Manufacturer"
            private const val DEFAULT_MODEL = "Model"
            private const val DEFAULT_RESOLUTION_PX = "Resolution (px)"
            private const val DEFAULT_RESOLUTION_DP = "Resolution (dp)"
            private const val DEFAULT_DENSITY = "Density (dpi)"
            private const val DEFAULT_ANDROID_VERSION = "Android SDK version"
        }
    }

    companion object {
        private val DEFAULT_THEME_RESOURCE_ID: Int? = null
        private val DEFAULT_NETWORK_LOG_DATE_FORMAT by lazy { SimpleDateFormat(LOG_TIME_FORMAT, Locale.ENGLISH) }
        private val DEFAULT_LOG_DATE_FORMAT by lazy { SimpleDateFormat(LOG_TIME_FORMAT, Locale.ENGLISH) }
        private val DEFAULT_GALLERY_DATE_FORMAT by lazy { SimpleDateFormat(GALLERY_DATE_FORMAT, Locale.ENGLISH) }
        private val DEFAULT_APPLY_INSETS: ((windowInsets: Insets) -> Insets)? = null
    }
}