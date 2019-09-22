package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays an expandable list of historical network activity. Use DebugMenuInterceptorContract to push a new message to the top of the list.
 * This module can only be added once.
 *
 * @param title - The title of the module. "Network activity" by default.
 * @param baseUrl - When not empty, all URL-s will have the specified String filtered out. Empty by default.
 * @param shouldShowHeaders - Whether of not the detail dialog should also contain the request / response headers. False by default.
 * @param maxMessageCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
 */
data class NetworkLoggingModule(
    val title: String = "Network activity",
    val baseUrl: String = "",
    val shouldShowHeaders: Boolean = false,
    val maxMessageCount: Int = 10,
    val shouldShowTimestamp: Boolean = false
) : DebugMenuModule {

    override val id = ID

    init {
        require(maxMessageCount > 0) { "DebugMenu: maxMessageCount must be larger than 0 for the NetworkLoggingModule." }
    }

    companion object {
        const val ID = "networkLogging"
    }
}