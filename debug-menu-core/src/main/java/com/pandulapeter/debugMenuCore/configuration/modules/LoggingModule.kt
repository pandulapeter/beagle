package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays an expandable list of custom log messages. Use DebugMenu.log() to push a new message to the top of the list. This can be useful for debugging analytics events.
 * This module can only be added once.
 *
 * @param title - The title of the module. "Logs" by default.
 * @param maxMessageCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
 */
data class LoggingModule(
    val title: String = "Logs",
    val maxMessageCount: Int = 10,
    val shouldShowTimestamp: Boolean = false
) : DebugMenuModule {

    override val id = ID

    init {
        require(maxMessageCount > 0) { "DebugMenu: maxMessageCount must be larger than 0 for the LoggingModule." }
    }

    companion object {
        const val ID = "logging"
    }
}