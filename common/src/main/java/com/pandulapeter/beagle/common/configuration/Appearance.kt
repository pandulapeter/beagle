package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StyleRes

/**
 * Specifies the appearance customization options for the debug menu. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID the menu should use. If null, each menu will take their Activity's theme. Null by default.
 * @param applyButtonText - The text on the Apply button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Apply" by default.
 * @param resetButtonText - The text on the Reset button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Reset" by default.
 * @param screenRecordingToastText - A Toast message displayed every time a screen recording is started, or null for no Toast. "Recording in progress. Tap on the notification to stop it." by default.
 * @param screenCaptureServiceNotificationTitle - Recording the screen for screenshot images or videos requires a foreground service with a notification. The title for the notification, by default is "Recording…".
 * @param screenCaptureServiceNotificationContent - The content for the notification described for the [screenCaptureServiceNotificationTitle] parameter. "Tap on this notification when done." by default.
 */
data class Appearance(
    @StyleRes val themeResourceId: Int? = null,
    val applyButtonText: CharSequence = "Apply",
    val resetButtonText: CharSequence = "Reset",
    val screenRecordingToastText: CharSequence? = "Recording in progress. Tap on the notification to stop it.",
    val screenCaptureServiceNotificationTitle: CharSequence = "Recording…",
    val screenCaptureServiceNotificationContent: CharSequence = "Tap on this notification when done."
)