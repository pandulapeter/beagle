package com.pandulapeter.beagle.implementation.util

internal fun consume(action: () -> Unit) = true.also { action() }