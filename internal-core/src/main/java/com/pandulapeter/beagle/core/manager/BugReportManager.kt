package com.pandulapeter.beagle.core.manager

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.core.util.extension.createZipFile
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.getCrashLogsFolder
import com.pandulapeter.beagle.core.util.getLifecycleLogsFolder
import com.pandulapeter.beagle.core.util.getLogsFolder
import com.pandulapeter.beagle.core.util.getMediaFolder
import com.pandulapeter.beagle.core.util.getNetworkLogsFolder
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class BugReportManager {

    fun openBugReportingScreen() {
        BeagleCore.implementation.currentActivity?.run {
            startActivity(BugReportActivity.newIntent(this))
        }
    }

    fun shareBugReport() {
        val filePaths = mutableListOf<String>()

        BeagleCore.implementation.currentActivity?.let { context ->

            GlobalScope.launch {
                // Media
                val mediaFileIds = context.getScreenCapturesFolder().listFiles().orEmpty().toList().sortedByDescending { it.lastModified() }.map { it.name }
                filePaths.addAll(getMediaFolder(mediaFileIds, context))

                // Crash logs
                val crashLogEntries = BeagleCore.implementation.getCrashLogEntriesInternal()
                filePaths.addAll(getCrashLogsFolder(crashLogEntries.map { it.id }, crashLogEntries, context))

                // Network log files
                val networkLogEntries = BeagleCore.implementation.getNetworkLogEntriesInternal()
                filePaths.addAll(getNetworkLogsFolder(networkLogEntries.map { it.id }, networkLogEntries, context))


                val logEntries = BeagleCore.implementation.behavior.bugReportingBehavior.logLabelSectionsToShow.map { label ->
                    label to BeagleCore.implementation.getLogEntriesInternal(label)
                }.toMap()
                val selectedLogIds = BeagleCore.implementation.behavior.bugReportingBehavior.logLabelSectionsToShow.map { label ->
                    label to BeagleCore.implementation.getLogEntriesInternal(label).map { it.id }
                }.toMap()
                // Log files
                filePaths.addAll(getLogsFolder(selectedLogIds, logEntries, context))

                // Lifecycle logs
                val lifecycleLogEntries = BeagleCore.implementation.getLifecycleLogEntriesInternal(
                    BeagleCore.implementation.behavior.bugReportingBehavior.lifecycleSectionEventTypes
                )
                filePaths.addAll(getLifecycleLogsFolder(lifecycleLogEntries.map { it.id }, lifecycleLogEntries, context))

                // Create the zip file
                context.createZipFile(
                    filePaths = filePaths,
                    zipFileName = "${BeagleCore.implementation.behavior.bugReportingBehavior.getBugReportFileName(currentTimestamp)}.zip",
                )?.let { uri ->
                    context.shareFile(uri, "application/zip")
                }
            }
        }
    }
}