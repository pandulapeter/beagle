package com.pandulapeter.debugMenu.utils

internal data class NetworkEvent(
    val isOutgoing: Boolean,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)