package com.pandulapeter.beagle.core.manager

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.Process
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.core.util.ExceptionHandlerService
import com.pandulapeter.beagle.core.util.model.CrashLogEntry

internal class ExceptionHandler(
    private val service: Messenger
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        val limit = BeagleCore.implementation.behavior.bugReportingBehavior.logRestoreLimit
        service.send(Message().apply {
            data = ExceptionHandlerService.createBundle(
                crashLogEntry = CrashLogEntry(
                    id = randomId,
                    exception = e.message ?: e::class.java.simpleName,
                    stacktrace = e.stackTraceToString(),
                    timestamp = currentTimestamp
                ),
                logs = BeagleCore.implementation.getLogEntries(null).take(limit),
                networkLogs = BeagleCore.implementation.getNetworkLogEntries().take(limit),
                lifecycleLogs = BeagleCore.implementation.getLifecycleLogEntries(BeagleCore.implementation.behavior.bugReportingBehavior.lifecycleSectionEventTypes).take(limit)
            )
        })
        Process.killProcess(Process.myPid())
    }

    private object InitializationManager {

        private var isInitialized: Boolean = false

        private val connection = object : android.content.ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) =
                Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(Messenger(service)))

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