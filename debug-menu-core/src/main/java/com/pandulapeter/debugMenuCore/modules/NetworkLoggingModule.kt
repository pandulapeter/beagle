package com.pandulapeter.debugMenuCore.modules

/**
 * Displays an expandable list of historical network activity. Use DebugMenu.logNetworkEvent() to push a new message to the top of the list.
 * @param title - The title of the module. "Network activity" by default.
 * @param maxMessageCount - The maximum number of messages that will appear when expanded. 10 by default.
 */
data class NetworkLoggingModule(
    val title: String = "Network activity",
    val maxMessageCount: Int = 10
) : DebugMenuModule {

    init {
        require(maxMessageCount > 0) { "DebugMenu: maxMessageCount must be larger than 0 for the NetworkLoggingModule." }
    }
}