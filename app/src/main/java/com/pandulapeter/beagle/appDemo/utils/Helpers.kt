package com.pandulapeter.beagle.appDemo.utils

fun consume(callback: () -> Unit) = true.also { callback() }