package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

internal data class NetworkLogEntry(
    override val id: String,
    val isOutgoing: Boolean,
    val payload: String,
    val headers: List<String> = emptyList(),
    val url: String,
    val duration: Long? = null,
    val timestamp: Long = currentTimestamp
) : BeagleListItemContract {

    override val title = Text.CharSequence(url)
}