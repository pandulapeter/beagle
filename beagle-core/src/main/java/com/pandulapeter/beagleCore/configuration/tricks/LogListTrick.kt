package com.pandulapeter.beagleCore.configuration.tricks

/**
 * Displays an expandable list of your custom logs. An example use case could be logging analytics events.
 * Use Beagle.log() to push a new message to the top of the list.
 * This module can only be added once.
 *
 * @param title - The title of the module. "Logs" by default.
 * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
 * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
 * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class LogListTrick(
    override val title: CharSequence = "Logs",
    val maxItemCount: Int = 10,
    val shouldShowTimestamp: Boolean = false,
    override val isInitiallyExpanded: Boolean = false
) : ExpandableTrick {

    override val id = ID

    init {
        require(maxItemCount > 0) { "Beagle: maxItemCount must be larger than 0 for the LogListTrick." }
    }

    companion object {
        const val ID = "logging"
    }
}

@Suppress("unused")
typealias LogListModule = LogListTrick