package com.pandulapeter.beagle.core.manager

import android.app.Application
import android.os.Process
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.core.util.ExceptionHandlerService
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import kotlin.system.exitProcess

internal class ExceptionHandler private constructor(
    private val application: Application
) : Thread.UncaughtExceptionHandler {

    init {
        application.startService(ExceptionHandlerService.getStartIntent(application))
    }

    override fun uncaughtException(thread: Thread, excepton: Throwable) {
        try {
            val limit = BeagleCore.implementation.behavior.bugReportingBehavior.logRestoreLimit
            application.startService(
                ExceptionHandlerService.getStartIntent(
                    context = application,
                    crashLogEntry = CrashLogEntry(
                        id = randomId,
                        exception = excepton.message ?: excepton::class.java.simpleName,
                        stacktrace = excepton.stackTraceToString(),
                        timestamp = currentTimestamp
                    ),
                    logs = BeagleCore.implementation.getLogEntries(null).take(limit),
                    networkLogs = BeagleCore.implementation.getNetworkLogEntries().take(limit),
                    lifecycleLogs = BeagleCore.implementation.getLifecycleLogEntries(BeagleCore.implementation.behavior.bugReportingBehavior.lifecycleSectionEventTypes).take(limit)
                )
            )
        } catch (e: Exception) {
        } finally {
            defaultExceptionHandler.let { defaultExceptionHandler ->
                if (defaultExceptionHandler != null && defaultExceptionHandler::class.java.canonicalName?.startsWith("com.android.internal.os") != true) {
                    defaultExceptionHandler.uncaughtException(thread, excepton)
                }
                Process.killProcess(Process.myPid())
                exitProcess(10)
            }
        }
    }

    companion object {
        private var isInitialized = false
        private var defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null

        fun initialize(application: Application) {
            if (!isInitialized) {
                isInitialized = true
                Thread.getDefaultUncaughtExceptionHandler()?.let {
                    if (it !is ExceptionHandler) {
                        defaultExceptionHandler = it
                    }
                }
                Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(application))
            }
        }
    }
}