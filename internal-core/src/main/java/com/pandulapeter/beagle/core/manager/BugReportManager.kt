package com.pandulapeter.beagle.core.manager

import android.net.Uri
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.model.CrashLogEntry
import com.pandulapeter.beagle.commonBase.model.LifecycleLogEntry
import com.pandulapeter.beagle.commonBase.model.LogEntry
import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry
import com.pandulapeter.beagle.core.util.extension.createBugReportTextFile
import com.pandulapeter.beagle.core.util.extension.createZipFile
import com.pandulapeter.beagle.core.util.extension.getBugReportsFolder
import com.pandulapeter.beagle.core.util.extension.getCrashLogFilePaths
import com.pandulapeter.beagle.core.util.extension.getLifecycleLogFilePaths
import com.pandulapeter.beagle.core.util.extension.getLogFilePaths
import com.pandulapeter.beagle.core.util.extension.getMediaFilePaths
import com.pandulapeter.beagle.core.util.extension.getNetworkLogFilePaths
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.generateBuildInformation
import com.pandulapeter.beagle.core.util.generateDeviceInformation
import com.pandulapeter.beagle.core.util.realPath
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

internal class BugReportManager {

    fun openBugReportingScreen() {
        BeagleCore.implementation.currentActivity?.run {
            startActivity(
                BugReportActivity.newIntent(
                    context = this,
                    themeResourceId = BeagleCore.implementation.appearance.themeResourceId
                )
            )
        }
    }

    fun shareBugReport(
        shouldIncludeMediaFile: (File) -> Boolean,
        shouldIncludeCrashLogEntry: (CrashLogEntry) -> Boolean,
        shouldIncludeNetworkLogEntry: (NetworkLogEntry) -> Boolean,
        shouldIncludeLogEntry: (LogEntry) -> Boolean,
        shouldIncludeLifecycleLogEntry: (LifecycleLogEntry) -> Boolean,
        shouldIncludeBuildInformation: Boolean,
        shouldIncludeDeviceInformation: Boolean,
        extraDataToInclude: String,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.Default) {
            val filePaths = mutableListOf<String>()
            val fileName = BeagleCore.implementation.behavior.bugReportingBehavior.getBugReportFileName(currentTimestamp)
            BeagleCore.implementation.currentActivity?.let { context ->

                // Media files
                filePaths.addAll(
                    context.getMediaFilePaths(
                        ids = context.getScreenCapturesFolder().listFiles().orEmpty().toList()
                            .sortedByDescending { it.lastModified() }
                            .filter(shouldIncludeMediaFile)
                            .map { it.name }
                    )
                )

                // Crash log files
                filePaths.addAll(
                    context.getCrashLogFilePaths(
                        ids = BeagleCore.implementation.getCrashLogEntries()
                            .filter(shouldIncludeCrashLogEntry)
                            .map { it.id }
                    )
                )

                // Network log files
                filePaths.addAll(
                    context.getNetworkLogFilePaths(
                        ids = BeagleCore.implementation.getNetworkLogEntries()
                            .filter(shouldIncludeNetworkLogEntry)
                            .map { it.id }
                    )
                )

                // Log files
                filePaths.addAll(
                    context.getLogFilePaths(
                        ids = BeagleCore.implementation.getLogEntries()
                            .filter(shouldIncludeLogEntry)
                            .map { it.id }
                    )
                )

                // Lifecycle log files
                filePaths.addAll(
                    context.getLifecycleLogFilePaths(
                        ids = BeagleCore.implementation.getLifecycleLogEntries()
                            .filter(shouldIncludeLifecycleLogEntry)
                            .map { it.id }
                    )
                )

                // Build information
                var content = ""
                val buildInformation = BeagleCore.implementation.behavior.bugReportingBehavior.buildInformation(context.application).generateBuildInformation(context)
                if (shouldIncludeBuildInformation && buildInformation.isNotBlank()) {
                    content = buildInformation.toString()
                }

                // Device information
                if (shouldIncludeDeviceInformation) {
                    val deviceInformation = generateDeviceInformation(context)
                    content = if (content.isBlank()) deviceInformation.toString() else "$content\n\n$deviceInformation"
                }

                // Extra content
                if (extraDataToInclude.isNotBlank()) {
                    content = if (content.isBlank()) extraDataToInclude else "$content\n\n$extraDataToInclude"
                }

                // Create the log file
                if (content.isNotBlank()) {
                    context.createBugReportTextFile(
                        fileName = "$fileName.txt",
                        content = content
                    )?.let { uri -> filePaths.add(uri.toPath(context.getBugReportsFolder())) }
                }

                // Create the zip file
                context.createZipFile(
                    filePaths = filePaths,
                    zipFileName = "$fileName.zip",
                )?.let { uri ->
                    launch(Dispatchers.Main) {
                        BeagleCore.implementation.behavior.bugReportingBehavior.onBugReportReady.let { onBugReportReady ->
                            if (onBugReportReady == null) {
                                context.shareFile(
                                    uri = uri,
                                    fileType = "application/zip",
                                    email = BeagleCore.implementation.behavior.bugReportingBehavior.emailAddress
                                )
                            } else {
                                onBugReportReady(uri)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Uri.toPath(folder: File): String = "${folder.canonicalPath}/$realPath"
}