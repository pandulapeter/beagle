package com.pandulapeter.beagle.commonBase.model

/**
 * Class representing a single log entry.
 *
 * @param id - The unique identifier of the entry.
 * @param label - The optional label of the entry.
 * @param message - The short summary message for the log.
 * @param payload - The optional, usually longer log payload.
 * @param timestamp - Timestamp of the moment the entry has been logged.
 */
data class LogEntry(
    val id: String,
    val label: String?,
    val message: String,
    val payload: String?,
    val timestamp: Long
)