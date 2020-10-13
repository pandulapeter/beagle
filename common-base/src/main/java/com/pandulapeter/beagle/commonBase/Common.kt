package com.pandulapeter.beagle.commonBase

import java.util.UUID

const val FILE_NAME_DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm-ss_SSS"
const val GALLERY_DATE_FORMAT = "yyyy-MM-dd"
const val LOG_TIME_FORMAT = "HH:mm:ss"

val randomId get() = UUID.randomUUID().toString()
val currentTimestamp get() = System.currentTimeMillis()