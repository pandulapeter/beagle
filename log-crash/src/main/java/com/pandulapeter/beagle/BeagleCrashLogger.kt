package com.pandulapeter.beagle

import android.app.Application
import com.pandulapeter.beagle.common.contracts.BeagleCrashLoggerContract
import com.pandulapeter.beagle.implementation.ExceptionHandler

object BeagleCrashLogger : BeagleCrashLoggerContract {

    override fun initialize(application: Application) = ExceptionHandler.initialize(application)
}