package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import java.util.UUID

internal data class NetworkLogEntry(
    val isOutgoing: Boolean,
    val body: String,
    val headers: List<String> = emptyList(),
    val url: String,
    val duration: Long? = null,
    override val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis()
) : BeagleListItemContract {

    override val title = url
}