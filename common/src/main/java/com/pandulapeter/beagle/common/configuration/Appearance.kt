package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StyleRes
import com.pandulapeter.beagle.common.configuration.Appearance.GalleryTexts
import com.pandulapeter.beagle.common.configuration.Appearance.GeneralTexts
import com.pandulapeter.beagle.common.configuration.Appearance.ScreenCaptureTexts
import com.pandulapeter.beagle.common.contracts.BeagleContract
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the appearance customization options for the debug menu. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID to be used for the debug menu as well as the Gallery screen. If null, the current Activity's theme will be used. Null by default.
 * @param generalTexts - Customize general UI strings, see [GeneralTexts].
 * @param screenCaptureTexts - Customize UI strings related to screen capture, see [ScreenCaptureTexts].
 * @param galleryTexts - Customize UI strings related to the Gallery, see [GalleryTexts].
 * @param networkEventTimestampFormatter - The formatter used for displaying the timestamp of network events on the detail dialog. Formats with "HH:mm:ss" by default.
 * @param galleryTimestampFormatter - The formatter used for displaying the timestamp of each day section in the gallery, or null if the sections should not be displayed at all. Formats with "yyyy-MM-dd" by default.
 * @param applyInsets - The library tries to handle window insets the best it can, but this might not work with your specific setup. To override the default behavior, provide a lambda that returns a new [Insets] object. Null by default.
 */
data class Appearance(
    @StyleRes val themeResourceId: Int? = null,
    val generalTexts: GeneralTexts = GeneralTexts(),
    val screenCaptureTexts: ScreenCaptureTexts = ScreenCaptureTexts(),
    val galleryTexts: GalleryTexts = GalleryTexts(),
    val networkEventTimestampFormatter: (Long) -> CharSequence = { defaultNetworkEventTimestampFormatter.format(it) },
    val galleryTimestampFormatter: ((Long) -> CharSequence)? = { defaultGalleryTimestampFormatter.format(it) },
    val applyInsets: ((windowInsets: Insets) -> Insets)? = null
) {

    /**
     * Holder for general purpose copies used in the debug menu.
     *
     * @param applyButtonText - The text on the Apply button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Apply" by default.
     * @param resetButtonText - The text on the Reset button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Reset" by default.
     * @param shareHint - The hint used for the Share icon of the Gallery screen and the Log detail dialog. "Share" by default.
     * @param shareErrorText - The text on the toast that appears when trying to share a large payload throws an exception. "Payload too large" by default.
     */
    data class GeneralTexts(
        val applyButtonText: Text = Text.CharSequence("Apply"),
        val resetButtonText: Text = Text.CharSequence("Reset"),
        val shareHint: Text = Text.CharSequence("Share"),
        val shareErrorText: Text = Text.CharSequence("Payload too large")
    )

    /**
     * Holder for copies related to screen capture.
     *
     * @param toastText - A Toast message displayed every time a screen recording is started, or null for no Toast. "Recording in progress. Tap on the notification to stop it." by default.
     * @param inProgressNotificationTitle - Recording the screen for screenshot images or videos requires a foreground service with a notification. The title for the notification, by default it's "Recording…".
     * @param inProgressNotificationContent - The content for the notification described for the [inProgressNotificationTitle] parameter. "Tap on this notification when done." by default.
     * @param readyNotificationTitle - The title for the notification displayed after a screen capture is done, by default it's "Screen captured".
     * @param readyNotificationContent - The content for the notification described for the [readyNotificationTitle] parameter. "Tap on this notification to open the Gallery." by default.
     * @param notificationChannelName - The name for the notification channel that handles all notifications related to screen capture. "Screen capture notifications" by default.
     */
    data class ScreenCaptureTexts(
        val toastText: Text? = Text.CharSequence("Recording in progress. Tap on the notification to stop it."),
        val inProgressNotificationTitle: Text = Text.CharSequence("Recording…"),
        val inProgressNotificationContent: Text = Text.CharSequence("Tap on this notification when done."),
        val readyNotificationTitle: Text = Text.CharSequence("Screen captured"),
        val readyNotificationContent: Text = Text.CharSequence("Tap on this notification to open the Gallery."),
        val notificationChannelName: Text = Text.CharSequence("Screen capture notifications")
    )

    /**
     * Holder for copies related to the Gallery screen.
     *
     * @param title - The title of the Gallery screen. "Gallery" by default.
     * @param noMediaMessage - The empty state text of the Gallery screen. "No media found" by default.
     * @param deleteHint - The hint used for the Delete icon of the Gallery screen. "Delete" by default.
     * @param deleteConfirmationMessageSingular - The message used for the Delete confirmation dialog of the Gallery screen when deleting one file. "Are you sure you want to delete this file?" by default.
     * @param deleteConfirmationMessagePlural - The message used for the Delete confirmation dialog of the Gallery screen when deleting multiple files. "Are you sure you want to delete these files?" by default.
     * @param deleteConfirmationPositive - The positive text used for the Delete confirmation dialog of the Gallery screen. "Delete" by default.
     * @param deleteConfirmationNegative - The positive text used for the Delete confirmation dialog of the Gallery screen. "Cancel" by default.
     */
    data class GalleryTexts(
        val title: Text = Text.CharSequence("Gallery"),
        val noMediaMessage: Text = Text.CharSequence("No media found"),
        val deleteHint: Text = Text.CharSequence("Delete"),
        val deleteConfirmationMessageSingular: Text = Text.CharSequence("Are you sure you want to delete this file?"),
        val deleteConfirmationMessagePlural: Text = Text.CharSequence("Are you sure you want to delete these files?"),
        val deleteConfirmationPositive: Text = Text.CharSequence("Delete"),
        val deleteConfirmationNegative: Text = Text.CharSequence("Cancel")
    )

    companion object {
        private val defaultNetworkEventTimestampFormatter by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }
        private val defaultGalleryTimestampFormatter by lazy { SimpleDateFormat(BeagleContract.GALLERY_DATE_FORMAT, Locale.ENGLISH) }
    }
}