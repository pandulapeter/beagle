package com.pandulapeter.beagle.logCrash.implementation

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import com.pandulapeter.beagle.core.util.crashLogEntryAdapter
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.LifecycleLogEntry
import com.pandulapeter.beagle.core.util.model.LogEntry
import com.pandulapeter.beagle.core.util.model.NetworkLogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.util.restoreModelAdapter
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import com.pandulapeter.beagle.utils.BundleArgumentDelegate

internal class ExceptionHandlerService : Service() {

    override fun onBind(intent: Intent?): IBinder? = Messenger(
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                applicationContext.startActivity(
                    BugReportActivity.newIntent(
                        context = applicationContext,
                        crashLogEntryToShowJson = message.data.crashLogEntryJson,
                        restoreModelJson = message.data.restoreModelJson
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    ).binder

    companion object {
        private var Bundle.crashLogEntryJson by BundleArgumentDelegate.String("crashLogEntryJson")
        private var Bundle.restoreModelJson by BundleArgumentDelegate.String("restoreModelJson")

        fun getMessage(
            crashLogEntry: CrashLogEntry,
            logs: List<LogEntry>,
            networkLogs: List<NetworkLogEntry>,
            lifecycleLogs: List<LifecycleLogEntry>
        ) = Message().apply {
            data = Bundle().apply {
                crashLogEntryJson = crashLogEntryAdapter.toJson(crashLogEntry)
                restoreModelJson = restoreModelAdapter.toJson(
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
