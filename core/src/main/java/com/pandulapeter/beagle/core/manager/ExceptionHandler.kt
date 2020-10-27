package com.pandulapeter.beagle.core.manager

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.Process
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.core.util.ExceptionHandlerService
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import kotlin.system.exitProcess

internal class ExceptionHandler(
    private val service: Messenger
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, excepton: Throwable) {
        try {
            val limit = BeagleCore.implementation.behavior.bugReportingBehavior.logRestoreLimit
            service.send(Message().apply {
                data = ExceptionHandlerService.createBundle(
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
            })
        } catch (_: Exception) {
        } finally {
            InitializationManager.defaultExceptionHandler.let { defaultExceptionHandler ->
                if (defaultExceptionHandler != null && defaultExceptionHandler::class.java.canonicalName?.startsWith("com.android.internal.os") != true) {
                    defaultExceptionHandler.uncaughtException(thread, excepton)
                }
                Process.killProcess(Process.myPid())
                exitProcess(10)
            }
        }
    }

    private object InitializationManager {

        private var isInitialized: Boolean = false
        var defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null
            private set

        private val connection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Thread.getDefaultUncaughtExceptionHandler()?.let {
                    if (it !is ExceptionHandler) {
                        defaultExceptionHandler = it
                    }
                }
                Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(Messenger(service)))
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit
        }

        fun init(applicationContext: Context) {
            if (isInitialized) {
                return
            }
            isInitialized = true
            val intent = Intent(applicationContext, ExceptionHandlerService::class.java)
            applicationContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    companion object {
        fun initialize(application: Application) = InitializationManager.init(application)
    }
}