package com.pandulapeter.beagle.logCrash

import android.app.Application
import com.pandulapeter.beagle.common.contracts.BeagleCrashLoggerContract
import com.pandulapeter.beagle.logCrash.implementation.ExceptionHandler

object BeagleCrashLogger : BeagleCrashLoggerContract {

    override fun initialize(application: Application) = ExceptionHandler.initialize(application)
}