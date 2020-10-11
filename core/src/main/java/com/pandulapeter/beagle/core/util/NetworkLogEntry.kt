package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module

internal data class NetworkLogEntry(
    val isOutgoing: Boolean,
    val payload: String,
    val headers: List<String> = emptyList(),
    val url: String,
    val duration: Long? = null,
    val timestamp: Long = System.currentTimeMillis()
) : BeagleListItemContract {

    override val title = Text.CharSequence(url)
    override val id: String = Module.randomId
}