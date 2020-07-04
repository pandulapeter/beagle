package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import java.util.UUID

internal data class NetworkLogEntry(
    val message: String,
    val payload: String,
    override val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis()
) : BeagleListItemContract {

    override val title = message
}