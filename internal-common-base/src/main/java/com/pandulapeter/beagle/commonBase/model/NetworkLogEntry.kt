package com.pandulapeter.beagle.commonBase.model

/**
 * Class representing a single network log entry.
 *
 * @param id - The unique identifier of the entry.
 * @param isOutgoing - True for outgoing requests, false for incoming responses.
 * @param url - The URL of the event (also contains the REST function type or error code).
 * @param payload - The event payload.
 * @param headers - The list of headers, where every item is formatted like this: "[key] value".
 * @param duration - For responses, the duration of the entire call in milliseconds.
 * @param timestamp - Timestamp of the moment the entry has been logged.
 */
data class NetworkLogEntry(
    val id: String,
    val isOutgoing: Boolean,
    val url: String,
    val payload: String,
    val headers: List<String>,
    val duration: Long?,
    val timestamp: Long
)