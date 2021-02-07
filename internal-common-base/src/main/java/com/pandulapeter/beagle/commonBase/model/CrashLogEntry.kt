package com.pandulapeter.beagle.commonBase.model

/**
 * Class representing a single crash log entry.
 *
 * @param id - The unique identifier of the entry.
 * @param exception - The fully specified class name of the Exception.
 * @param stacktrace - The complete stacktrace of the crash.
 * @param timestamp - Timestamp of the moment the entry has been logged.
 */
data class CrashLogEntry(
    val id: String,
    val exception: String,
    val stacktrace: String,
    val timestamp: Long
)