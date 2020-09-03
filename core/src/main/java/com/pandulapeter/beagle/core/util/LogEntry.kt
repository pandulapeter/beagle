package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import java.util.UUID

internal data class LogEntry(
    val label: String?,
    val message: CharSequence,
    val payload: CharSequence?,
    override val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis()
) : BeagleListItemContract {

    override val title = message
}