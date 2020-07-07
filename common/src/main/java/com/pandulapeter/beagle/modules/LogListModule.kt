package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Displays an expandable list of log messages. An example use case could be logging analytics events.
 * Use Beagle.log() to push a new message to the top of the list.
 * Only a single instance of this module can be added for one specific tag.
 *
 * @param title - The title of the module. "Logs" by default.
 * @param tag - The tag for which the logs should be filtered, or null for no filtering. Null by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with "HH:mm:ss" by default.
 * @param isHorizontalScrollEnabled - When true, the payload dialog will scroll in both directions. If false, the text will be wrapped and only vertical scrolling will be supported. False by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class LogListModule(
    override val title: CharSequence = "Logs",
    val tag: String? = null,
    val maxItemCount: Int = 10,
    val timestampFormatter: ((Long) -> CharSequence)? = { defaultFormatter.format(it) },
    val isHorizontalScrollEnabled: Boolean = false,
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<LogListModule> {

    override val id = formatId(tag)

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        private val defaultFormatter by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }

        fun formatId(tag: String?) = "logList_$tag"
    }
}