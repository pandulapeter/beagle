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
import com.pandulapeter.beagle.core.util.crashLogEntryAdapter
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.util.restoreModelAdapter
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import kotlin.system.exitProcess

internal class ExceptionHandler private constructor(
    private val messenger: Messenger
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        try {
            messenger.send(
                ExceptionHandlerService.getMessage(
                    crashLogEntry = exception.toCrashLogEntry(),
                    logs = logsToRestore,
                    networkLogs = networkLogsToRestore,
                    lifecycleLogs = lifecycleLogsToRestore
                )
            )
            tryToCallDefaultExceptionHandler(thread, exception)
        } catch (_: Exception) {
        } finally {
            killProcess()
        }
    }

    companion object {
        private const val CRASH_LOOP_LIMIT = 1000L
        private var isInitialized = false
        private var defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null
        private var lastCrashTimestamp = 0L
        private val limit by lazy { BeagleCore.implementation.behavior.bugReportingBehavior.logRestoreLimit }
        private val logsToRestore
            get() = BeagleCore.implementation.getLogEntries(null).take(limit)
        private val networkLogsToRestore
            get() = BeagleCore.implementation.getNetworkLogEntries().take(limit)
        private val lifecycleLogsToRestore
            get() = BeagleCore.implementation.getLifecycleLogEntries(BeagleCore.implementation.behavior.bugReportingBehavior.lifecycleSectionEventTypes).take(limit)

        fun initialize(application: Application) {
            if (!isInitialized) {
                isInitialized = true
                Thread.getDefaultUncaughtExceptionHandler()?.let {
                    if (it !is ExceptionHandler) {
                        defaultExceptionHandler = it
                    }
                }
                Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
                    try {
                        application.startActivity(
                            BugReportActivity.newIntent(
                                context = application,
                                crashLogEntryToShowJson = crashLogEntryAdapter.toJson(exception.toCrashLogEntry()),
                                restoreModelJson = restoreModelAdapter.toJson(
                                    RestoreModel(
                                        logs = logsToRestore,
                                        networkLogs = networkLogsToRestore,
                                        lifecycleLogs = lifecycleLogsToRestore
                                    )
                                )
                            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        tryToCallDefaultExceptionHandler(thread, exception)
                    } catch (_: Exception) {
                    } finally {
                        killProcess()
                    }
                }
                application.bindService(
                    Intent(application, ExceptionHandlerService::class.java),
                    object : ServiceConnection {

                        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                            if (service != null) {
                                Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(Messenger(service)))
                            }
                        }

                        override fun onServiceDisconnected(name: ComponentName?) = Unit
                    },
                    Context.BIND_AUTO_CREATE
                )
            }
        }

        private fun Throwable.toCrashLogEntry() = CrashLogEntry(
            id = randomId,
            exception = message ?: this::class.java.simpleName,
            stacktrace = stackTraceToString(),
            timestamp = currentTimestamp
        )

        private fun tryToCallDefaultExceptionHandler(thread: Thread, exception: Throwable) {
            defaultExceptionHandler.let { defaultExceptionHandler ->
                if (currentTimestamp - lastCrashTimestamp > CRASH_LOOP_LIMIT
                    && defaultExceptionHandler != null
                    && defaultExceptionHandler::class.java.canonicalName?.startsWith("com.android.internal.os") != true
                ) {
                    lastCrashTimestamp = currentTimestamp
                    defaultExceptionHandler.uncaughtException(thread, exception)
                }
            }
        }

        private fun killProcess() {
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
    }
}