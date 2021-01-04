package com.pandulapeter.beagle.logCrash.implementation

import android.app.Application
import android.content.Intent
import android.os.Process
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.core.util.crashLogEntryAdapter
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.util.restoreModelAdapter
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import kotlin.system.exitProcess

internal class ExceptionHandler private constructor(
    private val application: Application
) : Thread.UncaughtExceptionHandler {

    private val limit by lazy { BeagleCore.implementation.behavior.bugReportingBehavior.logRestoreLimit }
    private val logsToRestore
        get() = BeagleCore.implementation.getLogEntries(null).take(limit)
    private val networkLogsToRestore
        get() = BeagleCore.implementation.getNetworkLogEntries().take(limit)
    private val lifecycleLogsToRestore
        get() = BeagleCore.implementation.getLifecycleLogEntries(BeagleCore.implementation.behavior.bugReportingBehavior.lifecycleSectionEventTypes).take(limit)
    private var lastCrashTimestamp = 0L

    override fun uncaughtException(thread: Thread, exception: Throwable) {
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

    companion object {
        private const val CRASH_LOOP_LIMIT = 500L
        private var isInitialized = false
        private var defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null

        fun initialize(application: Application) {
            if (!isInitialized) {
                Thread.getDefaultUncaughtExceptionHandler()?.let {
                    if (it !is ExceptionHandler) {
                        defaultExceptionHandler = it
                        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(application))
                        isInitialized = true
                    }
                }
            }
        }
    }
}