package com.pandulapeter.beagleCore.configuration.tricks

/**
 * Displays an expandable list of historical network activity.
 * Use BeagleInterceptorContract to push a new message to the top of the list.
 * This module can only be added once.
 *
 * @param title - The title of the module. "Network activity" by default.
 * @param baseUrl - When not empty, all URL-s will have the specified String filtered out. Empty by default.
 * @param shouldShowHeaders - Whether of not the detail dialog should also contain the request / response headers. False by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
 * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class NetworkLogListTrick(
    override val title: CharSequence = "Network activity",
    val baseUrl: String = "",
    val shouldShowHeaders: Boolean = false,
    val maxItemCount: Int = 10,
    val shouldShowTimestamp: Boolean = false,
    override val isInitiallyExpanded: Boolean = false
) : ExpandableTrick {

    override val id = ID

    init {
        require(maxItemCount > 0) { "Beagle: maxItemCount must be larger than 0 for the NetworkLogListTrick." }
    }

    companion object {
        const val ID = "networkLogging"
    }
}

@Suppress("unused")
typealias NetworkLogListModule = NetworkLogListTrick