package com.pandulapeter.beagle.core.util

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

    override fun onBind(intent: Intent?): IBinder? = Messenger(object : Handler(Looper.myLooper() ?: Looper.getMainLooper()) {

        //TODO: Doesn't get called in minified builds.
        override fun handleMessage(msg: Message) {
            crashLogEntryAdapter.fromJson(msg.data.crashLogEntry)?.let { crashLogEntry ->
                BeagleCore.implementation.logCrash(crashLogEntry)
                startActivity(
                    BugReportActivity.newIntent(
                        context = this@ExceptionHandlerService,
                        crashLogIdToShow = crashLogEntry.id,
                        restoreModel = msg.data.restoreModel
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }).binder

    companion object {
        private var Bundle.crashLogEntry by BundleArgumentDelegate.String("crashLogEntry")
        private var Bundle.restoreModel by BundleArgumentDelegate.String("restoreModel")

        fun getMessage(
            crashLogEntry: CrashLogEntry,
            logs: List<LogEntry>,
            networkLogs: List<NetworkLogEntry>,
            lifecycleLogs: List<LifecycleLogEntry>
        ) = Message().apply {
            data = Bundle().apply {
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
}
