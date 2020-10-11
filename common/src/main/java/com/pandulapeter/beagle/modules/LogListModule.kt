package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.LogListModule.Companion.DEFAULT_LABEL
import com.pandulapeter.beagle.modules.LogListModule.Companion.DEFAULT_MAX_ITEM_COUNT
import com.pandulapeter.beagle.modules.LogListModule.Companion.DEFAULT_TITLE
import com.pandulapeter.beagle.modules.LogListModule.Companion.formatId
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Displays an expandable list of log messages. An example use case could be logging analytics events.
 * Use Beagle.log() to push a new message to the top of the list.
 *
 * If you want to log from a non-Android module, you can include a different dependency. Check out this section for guidance: https://github.com/pandulapeter/beagle#logging
 *
 * Only a single instance of this module can be added for one specific label. Use the [formatId] function to get the module ID in function of the label.
 *
 * @param title - The title of the module. [DEFAULT_TITLE] by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. [DEFAULT_MAX_ITEM_COUNT] by default.
 * @param timestampFormatter - The formatter used for displaying the timestamp of each entry, or null if the timestamps should not be displayed at all. Formats with [BeagleContract.LOG_TIME_FORMAT] by default.
 * @param label - The label for which the logs should be filtered, or null for no filtering. [DEFAULT_LABEL] by default.
 * @param isHorizontalScrollEnabled - When true, the payload dialog will scroll in both directions. If false, the text will be wrapped and only vertical scrolling will be supported. [DEFAULT_IS_HORIZONTAL_SCROLL_ENABLED] by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 */
data class LogListModule(
    val title: Text = Text.CharSequence(DEFAULT_TITLE),
    val maxItemCount: Int = DEFAULT_MAX_ITEM_COUNT,
    val timestampFormatter: ((Long) -> CharSequence)? = { DEFAULT_DATE_FORMAT.format(it) },
    val label: String? = DEFAULT_LABEL,
    val isHorizontalScrollEnabled: Boolean = DEFAULT_IS_HORIZONTAL_SCROLL_ENABLED,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY
) : ExpandableModule<LogListModule> {

    override val id = formatId(label)

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        private const val DEFAULT_TITLE = "Logs"
        private const val DEFAULT_MAX_ITEM_COUNT = 10
        private val DEFAULT_DATE_FORMAT by lazy { SimpleDateFormat(BeagleContract.LOG_TIME_FORMAT, Locale.ENGLISH) }
        private val DEFAULT_LABEL: String? = null
        private const val DEFAULT_IS_HORIZONTAL_SCROLL_ENABLED = false
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false

        /**
         * Generates a module ID from a label.
         *
         * @param label - The label specific to the [LogListModule].
         */
        fun formatId(label: String?) = "logList_$label"
    }
}