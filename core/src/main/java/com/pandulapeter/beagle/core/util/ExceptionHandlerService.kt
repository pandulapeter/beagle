package com.pandulapeter.beagle.core.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.LifecycleLogEntry
import com.pandulapeter.beagle.core.util.model.LogEntry
import com.pandulapeter.beagle.core.util.model.NetworkLogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import com.pandulapeter.beagle.utils.BundleArgumentDelegate

internal class ExceptionHandlerService : Service() {

    //TODO: Doesn't work in minified builds.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getBundleExtra(BUNDLE)?.let { bundle ->
            crashLogEntryAdapter.fromJson(bundle.crashLogEntry)?.let { crashLogEntry ->
                BeagleCore.implementation.logCrash(crashLogEntry)
                application.startActivity(
                    BugReportActivity.newIntent(
                        context = application,
                        crashLogIdToShow = crashLogEntry.id,
                        restoreModel = bundle.restoreModel
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = throw IllegalStateException("Binding not supported.")

    companion object {
        private const val BUNDLE = "bundle"
        private var Bundle.crashLogEntry by BundleArgumentDelegate.String("crashLogEntry")
        private var Bundle.restoreModel by BundleArgumentDelegate.String("restoreModel")

        fun getStartIntent(
            context: Context
        ) = Intent(context, ExceptionHandlerService::class.java)

        fun getStartIntent(
            context: Context,
            crashLogEntry: CrashLogEntry,
            logs: List<LogEntry>,
            networkLogs: List<NetworkLogEntry>,
            lifecycleLogs: List<LifecycleLogEntry>
        ) = getStartIntent(context).putExtra(
            BUNDLE,
            createBundle(
                crashLogEntry = crashLogEntry,
                logs = logs,
                networkLogs = networkLogs,
                lifecycleLogs = lifecycleLogs
            )
        )

        private fun createBundle(
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
