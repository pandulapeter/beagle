package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ExpandableModule

/**
 * Displays an expandable list of log messages. An example use case could be logging analytics events.
 * Use Beagle.log() to push a new message to the top of the list.
 * Only a single instance of this module can be added for one specific tag.
 *
 * @param title - The title of the module. "Logs" by default.
 * @param tag - The tag for which the logs should be filtered, or null for no filtering. Null by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
 * @param isExpandedInitially - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class LogListModule(
    override val title: CharSequence = "Logs",
    val tag: String? = null,
    val maxItemCount: Int = 10,
    val shouldShowTimestamp: Boolean = false, //TODO: Not handled
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<LogListModule> {

    override val id = "logList_$tag"
    override val canExpand = true //TODO

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}