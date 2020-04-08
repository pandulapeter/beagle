package com.pandulapeter.beagleCore.implementation

data class ChangeEvent(
    val trickId: String,
    val event: () -> Unit
)