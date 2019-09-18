package com.pandulapeter.debugMenuCore.modules

/**
 * Displays an expandable list of custom log messages. Use DebugMenu.log() to push a new message to the top of the list.
 * @param title - The title of the module. "Logs" by default.
 * @param maxMessageCount - The maximum number of messages that will appear.
 */
data class LoggingModule(
    val title: String = "Logs",
    val maxMessageCount: Int = 10
) : DebugMenuModule {

    init {
        require(maxMessageCount > 0) { "DebugMenu: maxMessageCount must be larger than 0 for the LoggingModule." }
    }
}