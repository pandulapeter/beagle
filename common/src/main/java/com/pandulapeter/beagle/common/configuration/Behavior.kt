package com.pandulapeter.beagle.common.configuration

import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_EXCLUDED_PACKAGE_NAMES
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_HAPTIC_FEEDBACK_DURATION
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_LOGGER
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_NETWORK_LOGGERS
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_SHAKE_THRESHOLD
import com.pandulapeter.beagle.commonBase.BeagleLoggerContract
import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract
import com.pandulapeter.beagle.commonBase.FILE_NAME_DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the behavior customization options for the debug menu. Used as an optional argument of Beagle.initialize(). All parameters are optional.
 *
 * @param shakeThreshold - The threshold value above which the debug menu will be opened when the user shakes the device. Values between 5 - 25 work best (smaller values result in more sensitive detection). Set to null to disable shake detection. [DEFAULT_SHAKE_THRESHOLD] by default.
 * @param shakeHapticFeedbackDuration - The length of the vibration triggered when a shake is detected, in milliseconds. Set to 0 to disable haptic feedback. [DEFAULT_HAPTIC_FEEDBACK_DURATION] by default.
 * @param excludedPackageNames - The list of packages that contain Activities for which Beagle should not be triggered. [DEFAULT_EXCLUDED_PACKAGE_NAMES] by default (and the library also contains a hardcoded list, unrelated to this parameter).
 * @param logger - The [BeagleLoggerContract] implementation in case logging is used in a pure Java / Kotlin module. [DEFAULT_LOGGER] by default.
 * @param networkLoggers - The list of [BeagleNetworkLoggerContract] implementations for intercepting network events. [DEFAULT_NETWORK_LOGGERS] by default.
 * @param screenCaptureServiceNotificationChannelId - The ID for the notification channel that handles all notifications related to screen capture. [DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID] by default.
 * @param getLogFileName - The lambda used to generate log file names (without the extension) when sharing them. The arguments are the timestamp and a unique ID of the log. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format and the ID.
 * @param getNetworkLogFileName - The lambda used to generate network log file names (without the extension) when sharing them. The arguments are the timestamp and a unique ID of the log. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format and the ID.
 * @param getImageFileName - The lambda used to generate screenshot image file names (without the extension). The argument is the timestamp when the file was created. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format.
 * @param getVideoFileName - The lambda used to generate screen recording video file names (without the extension). The argument is the timestamp when the file was created. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format.
 * @param getButReportFileName - The lambda used to generate bug report file names (without the extension). The argument is the timestamp when the file was created. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format.
 */
data class Behavior(
    val shakeThreshold: Int? = DEFAULT_SHAKE_THRESHOLD,
    val shakeHapticFeedbackDuration: Long = DEFAULT_HAPTIC_FEEDBACK_DURATION,
    val excludedPackageNames: List<String> = DEFAULT_EXCLUDED_PACKAGE_NAMES,
    val logger: BeagleLoggerContract? = DEFAULT_LOGGER,
    val networkLoggers: List<BeagleNetworkLoggerContract> = DEFAULT_NETWORK_LOGGERS,
    val screenCaptureServiceNotificationChannelId: String = DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID,
    val getLogFileName: (timestamp: Long, id: String) -> String = { timestamp, id -> "log_${DEFAULT_LOG_FILE_NAME_DATE_FORMAT.format(timestamp)}_$id" },
    val getNetworkLogFileName: (timestamp: Long, id: String) -> String = { timestamp, id -> "networkLog_${DEFAULT_LOG_FILE_NAME_DATE_FORMAT.format(timestamp)}_$id" },
    val getImageFileName: (timestamp: Long) -> String = { timestamp -> "${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}_image" },
    val getVideoFileName: (timestamp: Long) -> String = { timestamp -> "${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}_video" },
    val getButReportFileName: (timestamp: Long) -> String = { timestamp -> "bugReport_${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}" }
) {
    companion object {
        private const val DEFAULT_SHAKE_THRESHOLD = 13
        private const val DEFAULT_HAPTIC_FEEDBACK_DURATION = 100L
        private val DEFAULT_EXCLUDED_PACKAGE_NAMES = emptyList<String>()
        private val DEFAULT_LOGGER: BeagleLoggerContract? = null
        private val DEFAULT_NETWORK_LOGGERS = emptyList<BeagleNetworkLoggerContract>()
        private const val DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID = "channel_beagle_screen_capture"
        private val DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT by lazy { SimpleDateFormat(FILE_NAME_DATE_TIME_FORMAT, Locale.ENGLISH) }
        private val DEFAULT_LOG_FILE_NAME_DATE_FORMAT by lazy { SimpleDateFormat(FILE_NAME_DATE_TIME_FORMAT, Locale.ENGLISH) }
    }
}