package com.pandulapeter.beagle.utils

fun consume(callback: () -> Unit) = true.also { callback() }