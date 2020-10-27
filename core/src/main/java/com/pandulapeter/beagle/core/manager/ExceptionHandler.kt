package com.pandulapeter.beagle.core.manager

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Messenger
import android.os.Process
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.core.util.ExceptionHandlerService
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import kotlin.system.exitProcess

internal class ExceptionHandler private constructor(
    private val messenger: Messenger
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, excepton: Throwable) {
        try {
            val limit = BeagleCore.implementation.behavior.bugReportingBehavior.logRestoreLimit
            messenger.send(
                ExceptionHandlerService.getMessage(
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
        } catch (_: Exception) {
        } finally {
            defaultExceptionHandler.let { defaultExceptionHandler ->
                if (currentTimestamp - lastCrashTimestamp > CRASH_LOOP_LIMIT
                    && defaultExceptionHandler != null
                    && defaultExceptionHandler::class.java.canonicalName?.startsWith("com.android.internal.os") != true
                ) {
                    lastCrashTimestamp = currentTimestamp
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
        private var lastCrashTimestamp = 0L
        private const val CRASH_LOOP_LIMIT = 1000L

        fun initialize(application: Application) {
            if (!isInitialized) {
                isInitialized = true
                Thread.getDefaultUncaughtExceptionHandler()?.let {
                    if (it !is ExceptionHandler) {
                        defaultExceptionHandler = it
                    }
                }
                application.bindService(
                    Intent(application, ExceptionHandlerService::class.java),
                    object : ServiceConnection {

                        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                            Thread.getDefaultUncaughtExceptionHandler()?.let {
                                if (it !is ExceptionHandler) {
                                    defaultExceptionHandler = it
                                }
                            }
                            Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(Messenger(service)))
                        }

                        override fun onServiceDisconnected(name: ComponentName?) = Unit
                    },
                    Context.BIND_AUTO_CREATE
                )
            }
        }
    }
}