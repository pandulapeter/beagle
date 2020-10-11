package com.pandulapeter.beagle.common.configuration

import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_EXCLUDED_PACKAGE_NAMES
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_HAPTIC_FEEDBACK_DURATION
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_LOGGER
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_NETWORK_LOGGERS
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_SHAKE_THRESHOLD
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_SHOULD_ALLOW_SELECTION_IN_DIALOGS
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.commonBase.BeagleLoggerContract
import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the behavior customization options for the debug menu. All parameters are optional.
 *
 * @param shakeThreshold - The threshold value above which the debug menu will be opened when the user shakes the device. Values between 5 - 25 work best (smaller values result in more sensitive detection). Set to null to disable shake detection. [DEFAULT_SHAKE_THRESHOLD] by default.
 * @param shakeHapticFeedbackDuration - The length of the vibration triggered when a shake is detected, in milliseconds. Set to 0 to disable haptic feedback. [DEFAULT_HAPTIC_FEEDBACK_DURATION] by default.
 * @param excludedPackageNames - The list of packages that contain Activities for which Beagle should not be triggered. [DEFAULT_EXCLUDED_PACKAGE_NAMES] by default (and the library also contains a hardcoded list, unrelated to this parameter).
 * @param logger - The [BeagleLoggerContract] implementation in case logging is used in a pure Java / Kotlin module. [DEFAULT_LOGGER] by default.
 * @param networkLoggers - The list of [BeagleNetworkLoggerContract] implementations for intercepting network events. [DEFAULT_NETWORK_LOGGERS] by default.
 * @param shouldAllowSelectionInDialogs - Whether or not text in dialogs (logs or network event logs) should be selectable. [DEFAULT_SHOULD_ALLOW_SELECTION_IN_DIALOGS] by default as selecting scrollable text is glitchy in most cases.
 * @param screenCaptureServiceNotificationChannelId - The ID for the notification channel that handles all notifications related to screen capture. [DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID] by default.
 * @param getImageFileName - The lambda used to generate screenshot image file names (without the extension). By default a name will be generated based on the current timestamp with the [BeagleContract.FILE_NAME_DATE_TIME_FORMAT] format.
 * @param getVideoFileName - The lambda used to generate screen recording video file names (without the extension). By default a name will be generated based on the current timestamp with the [BeagleContract.FILE_NAME_DATE_TIME_FORMAT] format.
 */
data class Behavior(
    val shakeThreshold: Int? = DEFAULT_SHAKE_THRESHOLD,
    val shakeHapticFeedbackDuration: Long = DEFAULT_HAPTIC_FEEDBACK_DURATION,
    val excludedPackageNames: List<String> = DEFAULT_EXCLUDED_PACKAGE_NAMES,
    val logger: BeagleLoggerContract? = DEFAULT_LOGGER,
    val networkLoggers: List<BeagleNetworkLoggerContract> = DEFAULT_NETWORK_LOGGERS,
    val shouldAllowSelectionInDialogs: Boolean = DEFAULT_SHOULD_ALLOW_SELECTION_IN_DIALOGS,
    val screenCaptureServiceNotificationChannelId: String = DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID,
    val getImageFileName: () -> String = { "${DEFAULT_FILE_NAME_DATE_FORMAT.format(System.currentTimeMillis())}_image" },
    val getVideoFileName: () -> String = { "${DEFAULT_FILE_NAME_DATE_FORMAT.format(System.currentTimeMillis())}_video" }
) {
    companion object {
        private const val DEFAULT_SHAKE_THRESHOLD = 13
        private const val DEFAULT_HAPTIC_FEEDBACK_DURATION = 100L
        private val DEFAULT_EXCLUDED_PACKAGE_NAMES = emptyList<String>()
        private val DEFAULT_LOGGER: BeagleLoggerContract? = null
        private val DEFAULT_NETWORK_LOGGERS = emptyList<BeagleNetworkLoggerContract>()
        private const val DEFAULT_SHOULD_ALLOW_SELECTION_IN_DIALOGS = false
        private const val DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID = "channel_beagle_screen_capture"
        private val DEFAULT_FILE_NAME_DATE_FORMAT by lazy { SimpleDateFormat(BeagleContract.FILE_NAME_DATE_TIME_FORMAT, Locale.ENGLISH) }
    }
}