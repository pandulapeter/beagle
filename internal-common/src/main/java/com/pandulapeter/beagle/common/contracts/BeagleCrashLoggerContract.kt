package com.pandulapeter.beagle.common.contracts

import android.app.Application

interface BeagleCrashLoggerContract {

    fun initialize(application: Application) = Unit
}