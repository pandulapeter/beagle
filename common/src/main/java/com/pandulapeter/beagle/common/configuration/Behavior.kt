package com.pandulapeter.beagle.common.configuration

import android.app.Application
import android.net.Uri
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_BUILD_INFORMATION
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_LOG_LABEL_SECTIONS_TO_SHOW
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_ON_BUG_REPORT_READY
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_SHOULD_CATCH_EXCEPTIONS
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_SHOULD_SHOW_CRASH_LOGS_SECTION
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_SHOULD_SHOW_GALLERY_SECTION
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_SHOULD_SHOW_LIFECYCLE_LOGS_SECTION
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_SHOULD_SHOW_METADATA_SECTION
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION
import com.pandulapeter.beagle.common.configuration.Behavior.BugReportingBehavior.Companion.DEFAULT_TEXT_INPUT_FIELDS
import com.pandulapeter.beagle.common.configuration.Behavior.Companion.DEFAULT_EXCLUDED_PACKAGE_NAMES
import com.pandulapeter.beagle.common.configuration.Behavior.LogBehavior
import com.pandulapeter.beagle.common.configuration.Behavior.LogBehavior.Companion.DEFAULT_LOGGERS
import com.pandulapeter.beagle.common.configuration.Behavior.NetworkLogBehavior
import com.pandulapeter.beagle.common.configuration.Behavior.NetworkLogBehavior.Companion.DEFAULT_BASE_URL
import com.pandulapeter.beagle.common.configuration.Behavior.NetworkLogBehavior.Companion.DEFAULT_NETWORK_LOGGERS
import com.pandulapeter.beagle.common.configuration.Behavior.ScreenCaptureBehavior
import com.pandulapeter.beagle.common.configuration.Behavior.ScreenCaptureBehavior.Companion.DEFAULT_ON_IMAGE_READY
import com.pandulapeter.beagle.common.configuration.Behavior.ScreenCaptureBehavior.Companion.DEFAULT_ON_VIDEO_READY
import com.pandulapeter.beagle.common.configuration.Behavior.ScreenCaptureBehavior.Companion.DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID
import com.pandulapeter.beagle.common.configuration.Behavior.ShakeDetectionBehavior
import com.pandulapeter.beagle.common.configuration.Behavior.ShakeDetectionBehavior.Companion.DEFAULT_HAPTIC_FEEDBACK_DURATION
import com.pandulapeter.beagle.common.configuration.Behavior.ShakeDetectionBehavior.Companion.DEFAULT_THRESHOLD
import com.pandulapeter.beagle.commonBase.BeagleLoggerContract
import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract
import com.pandulapeter.beagle.commonBase.FILE_NAME_DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Specifies the behavior customization options for the debug menu. Used as an optional argument of Beagle.initialize(). All parameters are optional.
 *
 * @param excludedPackageNames - The list of packages that contain Activities for which Beagle should not be triggered. [DEFAULT_EXCLUDED_PACKAGE_NAMES] by default (and the library also contains a hardcoded list, unrelated to this parameter).
 * @param shakeDetectionBehavior - Customize the shake detection behavior, see [ShakeDetectionBehavior].
 * @param logBehavior - Customize the logging behavior, see [LogBehavior].
 * @param networkLogBehavior - Customize the network logging behavior, see [NetworkLogBehavior].
 * @param screenCaptureBehavior - Customize the screen capture behavior, see [ScreenCaptureBehavior].
 * @param bugReportingBehavior - Customize the bug reporting behavior, see [BugReportingBehavior].
 */
data class Behavior(
    val excludedPackageNames: List<String> = DEFAULT_EXCLUDED_PACKAGE_NAMES,
    val shakeDetectionBehavior: ShakeDetectionBehavior = ShakeDetectionBehavior(),
    val logBehavior: LogBehavior = LogBehavior(),
    val networkLogBehavior: NetworkLogBehavior = NetworkLogBehavior(),
    val screenCaptureBehavior: ScreenCaptureBehavior = ScreenCaptureBehavior(),
    val bugReportingBehavior: BugReportingBehavior = BugReportingBehavior()
) {
    companion object {
        private val DEFAULT_EXCLUDED_PACKAGE_NAMES = emptyList<String>()
        private val DEFAULT_LOG_FILE_NAME_DATE_FORMAT by lazy { SimpleDateFormat(FILE_NAME_DATE_TIME_FORMAT, Locale.ENGLISH) }
        private val DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT by lazy { SimpleDateFormat(FILE_NAME_DATE_TIME_FORMAT, Locale.ENGLISH) }
    }

    /**
     * Configuration related to shake detection.
     *
     * @param threshold - The threshold value above which the debug menu will be opened when the user shakes the device. Values between 5 - 25 work best (smaller values result in more sensitive detection). Set to null to disable shake detection. [DEFAULT_THRESHOLD] by default.
     * @param hapticFeedbackDuration - The length of the vibration triggered when a shake is detected, in milliseconds. Set to 0 to disable haptic feedback. [DEFAULT_HAPTIC_FEEDBACK_DURATION] by default.
     */
    data class ShakeDetectionBehavior(
        val threshold: Int? = DEFAULT_THRESHOLD,
        val hapticFeedbackDuration: Long = DEFAULT_HAPTIC_FEEDBACK_DURATION,
    ) {

        companion object {
            private const val DEFAULT_THRESHOLD = 13
            private const val DEFAULT_HAPTIC_FEEDBACK_DURATION = 100L
        }
    }

    /**
     * Configuration related to logs.
     *
     * @param loggers - The list of [BeagleLoggerContract] implementations, useful when logging is needed in a pure Java / Kotlin modules. [DEFAULT_LOGGERS] by default.
     * @param getFileName - The lambda used to generate log file names (without the extension) when sharing them. The arguments are the timestamp and a unique ID of the log. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format and the ID.
     */
    data class LogBehavior(
        val loggers: List<BeagleLoggerContract> = DEFAULT_LOGGERS,
        val getFileName: (timestamp: Long, id: String) -> String = { timestamp, id -> "log_${DEFAULT_LOG_FILE_NAME_DATE_FORMAT.format(timestamp)}_$id" },
    ) {

        companion object {
            private val DEFAULT_LOGGERS = emptyList<BeagleLoggerContract>()
        }
    }

    /**
     * Configuration related to network logs.
     *
     * @param networkLoggers - The list of [BeagleNetworkLoggerContract] implementations for intercepting network events. [DEFAULT_NETWORK_LOGGERS] by default.
     * @param baseUrl - When not empty, all URL-s will have the specified String filtered out to reduce the amount of redundant information. [DEFAULT_BASE_URL] by default.
     * @param getFileName - The lambda used to generate network log file names (without the extension) when sharing them. The arguments are the timestamp and a unique ID of the log. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format and the ID.
     */
    data class NetworkLogBehavior(
        val networkLoggers: List<BeagleNetworkLoggerContract> = DEFAULT_NETWORK_LOGGERS,
        val baseUrl: String = DEFAULT_BASE_URL,
        val getFileName: (timestamp: Long, id: String) -> String = { timestamp, id -> "networkLog_${DEFAULT_LOG_FILE_NAME_DATE_FORMAT.format(timestamp)}_$id" }
    ) {

        companion object {
            private const val DEFAULT_BASE_URL = ""
            private val DEFAULT_NETWORK_LOGGERS = emptyList<BeagleNetworkLoggerContract>()
        }
    }

    /**
     * Configuration related to screen capture.
     *
     * @param serviceNotificationChannelId - The ID for the notification channel that handles all notifications related to screen capture. [DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID] by default.
     * @param getImageFileName - The lambda used to generate screenshot image file names (without the extension). The argument is the timestamp when the file was created. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format.
     * @param onImageReady - The lambda that gets invoked after the screenshot is ready, with the [Uri] pointing to the PNG file. The argument will be null if anything goes wrong. [DEFAULT_ON_IMAGE_READY] by default.
     * @param getVideoFileName - The lambda used to generate screen recording video file names (without the extension). The argument is the timestamp when the file was created. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format.
     * @param onVideoReady - The lambda that gets invoked after the recording is done, with the [Uri] pointing to the MP4 file. The argument will be null if anything goes wrong. [DEFAULT_ON_VIDEO_READY] by default.
     */
    data class ScreenCaptureBehavior(
        val serviceNotificationChannelId: String = DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID,
        val getImageFileName: (timestamp: Long) -> String = { timestamp -> "${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}_image" },
        val onImageReady: ((imageUri: Uri?) -> Unit)? = DEFAULT_ON_IMAGE_READY,
        val getVideoFileName: (timestamp: Long) -> String = { timestamp -> "${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}_video" },
        val onVideoReady: ((videoUri: Uri?) -> Unit)? = DEFAULT_ON_VIDEO_READY,
    ) {

        companion object {
            private const val DEFAULT_SCREEN_CAPTURE_SERVICE_NOTIFICATION_CHANNEL_ID = "channel_beagle_screen_capture"
            private val DEFAULT_ON_IMAGE_READY: ((Uri?) -> Unit)? = null
            private val DEFAULT_ON_VIDEO_READY: ((Uri?) -> Unit)? = null
        }
    }

    /**
     * Configuration related to bug reporting.
     *
     * @param shouldCatchExceptions - Whether or not the library should handle uncaught exceptions by logging them and opening the bug reporting screen. Warning: this interferes with other crash reporting solutions. [DEFAULT_SHOULD_CATCH_EXCEPTIONS] by default.
     * @param pageSize - The number of crash log / network log / log / lifecycle log entries to load for every page, before the "Show more" button is displayed. [DEFAULT_PAGE_SIZE] by default.
     * @param shouldShowGallerySection - Whether or not the gallery section should be added. [DEFAULT_SHOULD_SHOW_GALLERY_SECTION] by default.
     * @param shouldShowCrashLogsSection - Whether or not the section of crash logs should be added. [DEFAULT_SHOULD_SHOW_CRASH_LOGS_SECTION] by default.
     * @param shouldShowNetworkLogsSection - Whether or not the section of network logs should be added. [DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION] by default.
     * @param logLabelSectionsToShow - The list of log labels for which sections should be added. Setting a list containing null adds a section for all logs, without filtering. [DEFAULT_LOG_LABEL_SECTIONS_TO_SHOW] by default.
     * @param shouldShowLifecycleLogsSection - Whether or not the section of lifecycle logs should be added. [DEFAULT_SHOULD_SHOW_LIFECYCLE_LOGS_SECTION] by default.
     * @param shouldShowMetadataSection - Whether or not the metadata section (build information and device information) should be added. [DEFAULT_SHOULD_SHOW_METADATA_SECTION] by default.
     * @param buildInformation - The list of key-value pairs that should be attached to reports as build information. The library can't figure out many important things so it is recommended to override the default value. [DEFAULT_BUILD_INFORMATION] by default.
     * @param textInputFields - The list of free-text inputs, where each entry is a pair of the field's title and its default value. [DEFAULT_TEXT_INPUT_FIELDS] by default.
     * @param getCrashLogFileName - The lambda used to generate crash log file names (without the extension) when sharing them. The arguments are the timestamp and a unique ID of the log. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format and the ID.
     * @param getBugReportFileName - The lambda used to generate bug report file names (without the extension). The argument is the timestamp when the file was created. By default a name will be generated with the [FILE_NAME_DATE_TIME_FORMAT] format.
     * @param onBugReportReady - The lambda that gets invoked after the bug report is ready, with the [Uri] pointing to the ZIP file, or null for the default implementation that uses the system share sheet. [DEFAULT_ON_BUG_REPORT_READY] by default.
     */
    data class BugReportingBehavior(
        val shouldCatchExceptions: Boolean = DEFAULT_SHOULD_CATCH_EXCEPTIONS,
        val pageSize: Int = DEFAULT_PAGE_SIZE,
        val shouldShowGallerySection: Boolean = DEFAULT_SHOULD_SHOW_GALLERY_SECTION,
        val shouldShowCrashLogsSection: Boolean = DEFAULT_SHOULD_SHOW_CRASH_LOGS_SECTION,
        val shouldShowNetworkLogsSection: Boolean = DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION,
        val logLabelSectionsToShow: List<String?> = DEFAULT_LOG_LABEL_SECTIONS_TO_SHOW,
        val shouldShowLifecycleLogsSection: Boolean = DEFAULT_SHOULD_SHOW_LIFECYCLE_LOGS_SECTION,
        val shouldShowMetadataSection: Boolean = DEFAULT_SHOULD_SHOW_METADATA_SECTION,
        val buildInformation: (activity: Application?) -> List<Pair<Text, String>> = DEFAULT_BUILD_INFORMATION,
        val textInputFields: List<Pair<Text, Text>> = DEFAULT_TEXT_INPUT_FIELDS,
        val getCrashLogFileName: (timestamp: Long, id: String) -> String = { timestamp, id -> "crashLog_${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}_$id" },
        val getBugReportFileName: (timestamp: Long) -> String = { timestamp -> "bugReport_${DEFAULT_MEDIA_FILE_NAME_DATE_FORMAT.format(timestamp)}" },
        val onBugReportReady: ((bugReport: Uri?) -> Unit)? = DEFAULT_ON_BUG_REPORT_READY
    ) {

        companion object {
            private const val DEFAULT_SHOULD_CATCH_EXCEPTIONS = true
            private const val DEFAULT_PAGE_SIZE = 5
            private const val DEFAULT_SHOULD_SHOW_GALLERY_SECTION = true
            private const val DEFAULT_SHOULD_SHOW_CRASH_LOGS_SECTION = true
            private const val DEFAULT_SHOULD_SHOW_NETWORK_LOGS_SECTION = true
            private val DEFAULT_LOG_LABEL_SECTIONS_TO_SHOW: List<String?> = listOf(null)
            private const val DEFAULT_SHOULD_SHOW_LIFECYCLE_LOGS_SECTION = true
            private const val DEFAULT_SHOULD_SHOW_METADATA_SECTION = true
            private val DEFAULT_BUILD_INFORMATION: (Application?) -> List<Pair<Text, String>> = { application ->
                mutableListOf<Pair<Text, String>>().apply {
                    if (application != null) {
                        add("Package name".toText() to application.packageName)
                    }
                }
            }
            private val DEFAULT_TEXT_INPUT_FIELDS: List<Pair<Text, Text>> = listOf(
                "Issue title".toText() to "".toText(),
                "Issue description".toText() to "".toText()
            )
            private val DEFAULT_ON_BUG_REPORT_READY: ((Uri?) -> Unit)? = null
        }
    }
}