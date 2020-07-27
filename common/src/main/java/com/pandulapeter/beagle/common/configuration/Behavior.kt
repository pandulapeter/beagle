package com.pandulapeter.beagle.common.configuration

import com.pandulapeter.beagle.common.contracts.BeagleContract
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the behavior customization options for the debug menu. All parameters are optional.
 *
 * @param shakeThreshold - The threshold value above which the debug menu will be shown when the user shakes the device. Values between 5 - 20 work best (smaller values result in more sensitive detection). Set to null to disable shake detection. 12 by default.
 * @param excludedPackageNames - The list of packages that contain Activities for which Beagle should not be triggered. Empty by default (however the library also contains a hardcoded list).
 * @param screenCaptureServiceNotificationChannelId - Recording the screen for screenshot images or videos requires a foreground service with a notification. The ID for the notification channel, by default is "channel_beagle_screen_capture".
 * @param screenCaptureServiceNotificationChannelName - The name for the notification channel described for the [screenCaptureServiceNotificationChannelId] parameter. "Beagle screen recorder" by default.
 * @param getImageFileName - The lambda used to generate screenshot image file names. The default a name will be generated based on the current timestamp.
 * @param getVideoFileName - The lambda used to generate screen recording video file names. The default a name will be generated based on the current timestamp.
 */
data class Behavior(
    val shakeThreshold: Int? = 12,
    val excludedPackageNames: List<String> = emptyList(),
    val screenCaptureServiceNotificationChannelId: String = "channel_beagle_screen_capture",
    val screenCaptureServiceNotificationChannelName: CharSequence = "Beagle screen recorder",
    val getImageFileName: () -> String = { "image_${simpleDateFormat.format(System.currentTimeMillis())}.png" },
    val getVideoFileName: () -> String = { "video_${simpleDateFormat.format(System.currentTimeMillis())}.mp4" }
) {

    companion object {
        private val simpleDateFormat by lazy { SimpleDateFormat(BeagleContract.FILE_NAME_DATE_TIME_FORMAT, Locale.ENGLISH) }
    }
}