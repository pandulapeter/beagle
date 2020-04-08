package com.pandulapeter.beagle.models

internal data class ChangeEvent(
    val trickId: String,
    val event: () -> Unit
)