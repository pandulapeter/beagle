package com.pandulapeter.beagle.core.util

import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.list.cells.SectionHeaderCell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.delegates.LifecycleLogListDelegate
import com.pandulapeter.beagle.core.util.extension.createLogFile
import com.pandulapeter.beagle.core.util.extension.getLogsFolder
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableLifecycleLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableNetworkLogEntry
import com.pandulapeter.beagle.core.view.networkLogDetail.NetworkLogDetailDialogViewModel
import com.pandulapeter.beagle.modules.TextModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

inline fun runOnUiThread(crossinline action: () -> Any?) {
    GlobalScope.launch(Dispatchers.Main) { action() }
}

internal fun createTextModuleFromType(
    type: TextModule.Type,
    id: String,
    text: Text,
    isEnabled: Boolean,
    @DrawableRes icon: Int?,
    onItemSelected: (() -> Unit)?
) = when (type) {
    TextModule.Type.NORMAL -> TextCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
        icon = icon,
        onItemSelected = onItemSelected
    )
    TextModule.Type.SECTION_HEADER -> SectionHeaderCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
        icon = icon,
        onItemSelected = onItemSelected
    )
    TextModule.Type.BUTTON -> ButtonCell(
        id = id,
        text = text,
        isEnabled = isEnabled,
        icon = icon,
        onButtonPressed = onItemSelected
    )
}

internal fun getFolder(rootFolder: File, name: String): File {
    val folder = File(rootFolder, name)
    folder.mkdirs()
    return folder
}

fun getMediaFolder(selectedMediaFileIds: List<String>, context: Context) =
    selectedMediaFileIds
        .map { fileName -> context.getUriForFile(context.getScreenCapturesFolder().resolve(fileName)) }
        .toPaths(context.getScreenCapturesFolder())

suspend fun getCrashLogsFolder(selectedCrashLogIds: List<String>, allCrashLogEntries: List<SerializableCrashLogEntry>?, context: Context) =
    selectedCrashLogIds
        .mapNotNull { id ->
            allCrashLogEntries?.firstOrNull { it.id == id }?.let { entry ->
                context.createLogFile(
                    fileName = "${BeagleCore.implementation.behavior.bugReportingBehavior.getCrashLogFileName(currentTimestamp, entry.id)}.txt",
                    content = entry.getFormattedContents(BeagleCore.implementation.appearance.logShortTimestampFormatter).toString()
                )
            }
        }
        .toPaths(context.getLogsFolder())

suspend fun getNetworkLogsFolder(selectedNetworkLogIds: List<String>, allNetworkLogEntries: List<SerializableNetworkLogEntry>, context: Context) =
    selectedNetworkLogIds
        .mapNotNull { id ->
            allNetworkLogEntries.firstOrNull { it.id == id }?.let { entry ->
                context.createLogFile(
                    fileName = "${BeagleCore.implementation.behavior.networkLogBehavior.getFileName(currentTimestamp, entry.id)}.txt",
                    content = NetworkLogDetailDialogViewModel.createLogFileContents(
                        title = NetworkLogDetailDialogViewModel.createTitle(
                            isOutgoing = entry.isOutgoing,
                            url = entry.url
                        ),
                        metadata = NetworkLogDetailDialogViewModel.createMetadata(
                            context = context,
                            headers = entry.headers,
                            timestamp = entry.timestamp,
                            duration = entry.duration
                        ),
                        formattedJson = NetworkLogDetailDialogViewModel.formatJson(
                            json = entry.payload,
                            indentation = 4
                        )
                    )
                )
            }
        }
        .toPaths(context.getLogsFolder())

suspend fun getLogsFolder(selectedLogIds: Map<String?, List<String>>, allLogEntriesMap: Map<String?, List<SerializableLogEntry>>, context: Context) =
    allLogEntriesMap[null]?.let { allLogEntries ->
        selectedLogIds.flatMap { it.value }.distinct().mapNotNull { id -> allLogEntries.firstOrNull { it.id == id } }
    }.orEmpty().mapNotNull { entry ->
        context.createLogFile(
            fileName = "${BeagleCore.implementation.behavior.logBehavior.getFileName(currentTimestamp, entry.id)}.txt",
            content = entry.getFormattedContents(BeagleCore.implementation.appearance.logShortTimestampFormatter).toString()
        )
    }.toPaths(context.getLogsFolder())

suspend fun getLifecycleLogsFolder(selectedLifecycleLogIds: List<String>, allLifecycleLogEntries: List<SerializableLifecycleLogEntry>, context: Context) =
    selectedLifecycleLogIds
        .mapNotNull { id ->
            allLifecycleLogEntries.firstOrNull { it.id == id }?.let { entry ->
                context.createLogFile(
                    fileName = "${BeagleCore.implementation.behavior.lifecycleLogBehavior.getFileName(currentTimestamp, entry.id)}.txt",
                    content = LifecycleLogListDelegate.format(
                        entry = entry,
                        formatter = BeagleCore.implementation.appearance.logShortTimestampFormatter,
                        shouldDisplayFullNames = BeagleCore.implementation.behavior.lifecycleLogBehavior.shouldDisplayFullNames
                    ).toString()
                )
            }
        }
        .toPaths(context.getLogsFolder())

fun List<Uri>.toPaths(folder: File): List<String> = folder.canonicalPath.let { path -> map { "$path/${it.realPath}" } }

val Uri.realPath get() = path?.split("/")?.lastOrNull()