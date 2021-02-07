package com.pandulapeter.beagle.commonBase.model

/**
 * Class representing a single lifecycle log entry.
 *
 * @param id - The unique identifier of the entry.
 * @param name - The fully specified class name of the Fragment / Activity.
 * @param simpleName - The simple class name of the Fragment / Activity.
 * @param eventType - The type of lifecycle event.
 * @param hasSavedInstanceState - True or False for events where it makes sense, null otherwise.
 * @param timestamp - Timestamp of the moment the entry has been logged.
 */
data class LifecycleLogEntry(
    val id: String,
    val name: String,
    val simpleName: String,
    val eventType: String,
    val hasSavedInstanceState: Boolean?,
    val timestamp: Long
)