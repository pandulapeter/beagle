package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StyleRes
import com.pandulapeter.beagle.common.contracts.BeagleContract
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the appearance customization options for the debug menu. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID to be used for the debug menu as well as the Gallery screen. If null, the current Activity's theme will be used. Null by default.
 * @param applyButtonText - The text on the Apply button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Apply" by default.
 * @param resetButtonText - The text on the Reset button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Reset" by default.
 * @param screenRecordingToastText - A Toast message displayed every time a screen recording is started, or null for no Toast. "Recording in progress. Tap on the notification to stop it." by default.
 * @param screenCaptureServiceNotificationTitle - Recording the screen for screenshot images or videos requires a foreground service with a notification. The title for the notification, by default it's "Recording…".
 * @param screenCaptureServiceNotificationContent - The content for the notification described for the [screenCaptureServiceNotificationTitle] parameter. "Tap on this notification when done." by default.
 * @param screenCaptureGalleryNotificationTitle - The title for the notification displayed after a screen capture is done, by default it's "Screen captured".
 * @param screenCaptureGalleryNotificationContent - The content for the notification described for the [screenCaptureGalleryNotificationTitle] parameter. "Tap on this notification to open the Gallery." by default.
 * @param screenCaptureServiceNotificationChannelName - The name for the notification channel that handles all notifications related to screen capture. "Screen capture notifications" by default.
 * @param galleryTitle - The title of the Gallery screen. "Gallery" by default.
 * @param galleryNoMediaMessage - The empty state text of the Gallery screen. "No media found" by default.
 * @param galleryTimestampFormatter - The formatter used for displaying the timestamp of each day section in the gallery, or null if the sections should not be displayed at all. Formats with "yyyy-MM-dd" by default.
 * @param galleryShareHint - The hint used for the Share icon of the Gallery screen. "Share" by default.
 * @param galleryDeleteHint - The hint used for the Delete icon of the Gallery screen. "Delete" by default.
 * @param galleryDeleteConfirmationMessageSingular - The message used for the Delete confirmation dialog of the Gallery screen when deleting one file. "Are you sure you want to delete this file?" by default.
 * @param galleryDeleteConfirmationMessagePlural - The message used for the Delete confirmation dialog of the Gallery screen when deleting multiple files. "Are you sure you want to delete these files?" by default.
 * @param galleryDeleteConfirmationPositive - The positive text used for the Delete confirmation dialog of the Gallery screen. "Delete" by default.
 * @param galleryDeleteConfirmationNegative - The positive text used for the Delete confirmation dialog of the Gallery screen. "Cancel" by default.
 * @param shareErrorText - The text on the toast that appears when trying to share a large payload throws an exception. "Payload too large" by default.
 * @param applyInsets - The library tries to handle window insets the best it can, but this might not work with your specific setup. To override the default behavior, provide a lambda that returns a new [Insets] object. Null by default.
 */
data class Appearance(
    @StyleRes val themeResourceId: Int? = null,
    val applyButtonText: CharSequence = "Apply",
    val resetButtonText: CharSequence = "Reset",
    val screenRecordingToastText: CharSequence? = "Recording in progress. Tap on the notification to stop it.",
    val screenCaptureServiceNotificationTitle: CharSequence = "Recording…",
    val screenCaptureServiceNotificationContent: CharSequence = "Tap on this notification when done.",
    val screenCaptureGalleryNotificationTitle: CharSequence = "Screen captured",
    val screenCaptureGalleryNotificationContent: CharSequence = "Tap on this notification to open the Gallery.",
    val screenCaptureServiceNotificationChannelName: CharSequence = "Screen capture notifications",
    val galleryTitle: CharSequence = "Gallery",
    val galleryNoMediaMessage: CharSequence = "No media found",
    val galleryTimestampFormatter: ((Long) -> CharSequence)? = { defaultFormatter.format(it) },
    val galleryShareHint: CharSequence = "Share",
    val galleryDeleteHint: CharSequence = "Delete",
    val galleryDeleteConfirmationMessageSingular: String = "Are you sure you want to delete this file?",
    val galleryDeleteConfirmationMessagePlural: String = "Are you sure you want to delete these files?",
    val galleryDeleteConfirmationPositive: String = "Delete",
    val galleryDeleteConfirmationNegative: String = "Cancel",
    val shareErrorText: CharSequence = "Payload too large",
    val applyInsets: ((windowInsets: Insets) -> Insets)? = null
) {

    companion object {
        private val defaultFormatter by lazy { SimpleDateFormat(BeagleContract.GALLERY_DATE_FORMAT, Locale.ENGLISH) }
    }
}