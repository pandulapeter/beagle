package com.pandulapeter.beagle.commonBase

import java.util.UUID

val randomId get() = UUID.randomUUID().toString()
val currentTimestamp get() = System.currentTimeMillis()