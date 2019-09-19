package com.pandulapeter.debugMenu.models

import java.util.UUID

internal data class LogMessage(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val message: String,
    val tag: String?,
    val payload: String?
)