package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays an expandable list of custom log messages. Use DebugMenu.log() to push a new message to the top of the list. This can be useful for debugging analytics events.
 * This module can only be added once.
 *
 * @param title - The title of the module. "Logs" by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
 * @param isInitiallyExpanded - Whether or not the list should be expanded by default.
 */
data class LogListModule(
    override val title: CharSequence = "Logs",
    val maxItemCount: Int = 10,
    val shouldShowTimestamp: Boolean = false,
    override val isInitiallyExpanded: Boolean = false
) : ExpandableDebugMenuModule {

    override val id = ID

    init {
        require(maxItemCount > 0) { "DebugMenu: maxItemCount must be larger than 0 for the LogListModule." }
    }

    companion object {
        const val ID = "logging"
    }
}