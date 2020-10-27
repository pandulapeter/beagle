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
import com.pandulapeter.beagle.core.util.CrashLogEntry
import com.pandulapeter.beagle.core.util.ExceptionHandlerService

internal class ExceptionHandler(
    private val service: Messenger
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        service.send(Message().apply {
            data = ExceptionHandlerService.createBundle(
                appearance = BeagleCore.implementation.appearance,
                behavior = BeagleCore.implementation.behavior,
                crashLogEntry = CrashLogEntry(
                    id = randomId,
                    exception = e::class.java.simpleName,
                    stacktrace = e.stackTraceToString(),
                    timestamp = currentTimestamp
                )
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