package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

internal data class LogEntry(
    override val id: String,
    val label: String?,
    val message: CharSequence,
    val payload: CharSequence?,
    val timestamp: Long
) : BeagleListItemContract {

    override val title = Text.CharSequence(message)
}