package com.pandulapeter.beagle.core.util

import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.LifecycleLogEntry
import com.pandulapeter.beagle.core.util.model.LogEntry
import com.pandulapeter.beagle.core.util.model.NetworkLogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import com.pandulapeter.beagle.utils.BundleArgumentDelegate

internal class ExceptionHandlerService : Service() {

    private val handler by lazy { ExceptionHandler(applicationContext as Application) }

    override fun onBind(intent: Intent?): IBinder? = Messenger(handler).binder

    internal class ExceptionHandler(
        private val application: Application
    ) : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            crashLogEntryAdapter.fromJson(msg.data.crashLogEntry)?.let { crashLogEntry ->
                BeagleCore.implementation.logCrash(crashLogEntry)
                application.startActivity(
                    BugReportActivity.newIntent(
                        context = application,
                        crashLogIdToShow = crashLogEntry.id,
                        restoreModel = msg.data.restoreModel
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    companion object {
        private var Bundle.crashLogEntry by BundleArgumentDelegate.String("crashLogEntry")
        private var Bundle.restoreModel by BundleArgumentDelegate.String("restoreModel")

        fun createBundle(
            crashLogEntry: CrashLogEntry,
            logs: List<LogEntry>,
            networkLogs: List<NetworkLogEntry>,
            lifecycleLogs: List<LifecycleLogEntry>
        ) = Bundle().apply {
            this.crashLogEntry = crashLogEntryAdapter.toJson(crashLogEntry)
            restoreModel = restoreModelAdapter.toJson(
                RestoreModel(
                    logs = logs,
                    networkLogs = networkLogs,
                    lifecycleLogs = lifecycleLogs
                )
            )
        }
    }
}
