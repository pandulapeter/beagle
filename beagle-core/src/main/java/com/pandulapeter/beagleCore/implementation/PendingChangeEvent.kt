package com.pandulapeter.beagleCore.implementation

data class PendingChangeEvent(
    val trickId: String,
    val apply: () -> Unit,
    val reset: () -> Unit
)