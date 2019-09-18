package com.pandulapeter.debugMenu.models

import java.util.UUID

internal data class NetworkEvent(
    val id: String = UUID.randomUUID().toString(),
    val isOutgoing: Boolean,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)