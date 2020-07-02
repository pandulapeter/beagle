package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.BeagleCore

internal class PendingUpdateManager {

    val hasPendingUpdates get() = _pendingUpdates.isNotEmpty()
    private val _pendingUpdates = mutableListOf<Event>()

    fun applyPendingChanges() {
        //TODO
        BeagleCore.implementation.refresh()
    }

    fun resetPendingChanges() {
        //TODO
        BeagleCore.implementation.refresh()
    }

    data class Event(
        val trickId: String,
        val apply: () -> Unit,
        val reset: () -> Unit
    )
}