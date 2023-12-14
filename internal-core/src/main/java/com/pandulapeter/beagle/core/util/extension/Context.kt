package com.pandulapeter.beagle.core.util.extension

import android.content.Context
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.view.ContextThemeWrapper
import androidx.core.content.FileProvider
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.core.list.delegates.LifecycleLogListDelegate
import com.pandulapeter.beagle.core.util.crashLogEntryAdapter
import com.pandulapeter.beagle.core.util.getFolder
import com.pandulapeter.beagle.core.util.logEntryAdapter
import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.core.util.toPaths
import com.pandulapeter.beagle.core.view.networkLogDetail.NetworkLogDetailDialogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal fun Context.registerSensorEventListener(sensorEventListener: SensorEventListener) = (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
    registerListener(sensorEventListener, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
} ?: false

internal fun Context.unregisterSensorEventListener(sensorEventListener: SensorEventListener) {
    (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.unregisterListener(sensorEventListener)
}

fun Context.applyTheme() = BeagleCore.implementation.appearance.themeResourceId?.let { ContextThemeWrapper(this, it) } ?: this

internal fun Context.getUriForFile(file: File) = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".beagle.fileProvider", file)

internal suspend fun Context.createScreenshotFromBitmap(bitmap: Bitmap, fileName: String): Uri? = withContext(Dispatchers.IO) {
    val file = createScreenCaptureFile(fileName)
    try {
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        getUriForFile(file)
    } catch (_: IOException) {
        null
    }
}

private const val SCREEN_CAPTURES_FOLDER_NAME = "beagleScreenCaptures"

internal fun Context.getScreenCapturesFolder() = getFilesFolder(SCREEN_CAPTURES_FOLDER_NAME)

internal fun Context.createScreenCaptureFile(fileName: String) = File(getScreenCapturesFolder(), fileName)

private fun Context.getFilesFolder(name: String) = getFolder(filesDir, name)

private const val LOGS_FOLDER_NAME = "beagleLogs"

internal fun Context.getPersistedLogsFolder() = getFilesFolder(LOGS_FOLDER_NAME)

private fun Context.createPersistedLogFile(fileName: String) = File(getPersistedLogsFolder(), fileName)

internal suspend fun readLogEntryFromLogFile(file: File): SerializableLogEntry? = withContext(Dispatchers.IO) {
    try {
        val logEntry: SerializableLogEntry?
        FileReader(file).run {
            logEntry = try {
                logEntryAdapter.fromJson(readText())
            } catch (_: Exception) {
                null
            } finally {
                close()
            }
        }
        logEntry
    } catch (e: IOException) {
        null
    }
}

internal const val LOG_PREFIX = "log_"

internal suspend fun Context.createPersistedLogFile(logEntry: SerializableLogEntry) = withContext(Dispatchers.IO) {
    val file = createPersistedLogFile("$LOG_PREFIX${logEntry.id}.txt")
    try {
        FileWriter(file).run {
            write(logEntryAdapter.toJson(logEntry))
            flush()
            close()
        }
    } catch (_: IOException) {
    }
}

internal suspend fun readCrashLogEntryFromLogFile(file: File): SerializableCrashLogEntry? = withContext(Dispatchers.IO) {
    try {
        val crashLogEntry: SerializableCrashLogEntry?
        FileReader(file).run {
            crashLogEntry = try {
                crashLogEntryAdapter.fromJson(readText())
            } catch (_: Exception) {
                null
            } finally {
                close()
            }
        }
        crashLogEntry
    } catch (e: IOException) {
        null
    }
}

internal const val CRASH_LOG_PREFIX = "crashLog_"

internal suspend fun Context.createPersistedCrashLogFile(crashLogEntry: SerializableCrashLogEntry) = withContext(Dispatchers.IO) {
    val file = createPersistedLogFile("$CRASH_LOG_PREFIX${crashLogEntry.timestamp}.txt")
    try {
        FileWriter(file).run {
            write(crashLogEntryAdapter.toJson(crashLogEntry))
            flush()
            close()
        }
    } catch (_: IOException) {
    }
}

internal fun Context.getLogsFolder() = getCacheFolder(LOGS_FOLDER_NAME)

internal fun Context.createLogFile(fileName: String) = File(getLogsFolder(), fileName)

internal suspend fun Context.createLogFile(fileName: String, content: String): Uri? = withContext(Dispatchers.IO) {
    val file = createLogFile(fileName)
    try {
        FileWriter(file).run {
            write(content)
            flush()
            close()
        }
        getUriForFile(file)
    } catch (e: IOException) {
        null
    }
}

private const val BUG_REPORTS_FOLDER_NAME = "beagleBugReports"

internal fun Context.getBugReportsFolder() = getCacheFolder(BUG_REPORTS_FOLDER_NAME)

internal fun Context.createBugReportsFile(fileName: String) = File(getBugReportsFolder(), fileName)

internal suspend fun Context.createBugReportTextFile(fileName: String, content: String): Uri? = withContext(Dispatchers.IO) {
    val file = createBugReportsFile(fileName)
    try {
        FileWriter(file).run {
            write(content)
            flush()
            close()
        }
        getUriForFile(file)
    } catch (e: IOException) {
        null
    }
}

private fun Context.getCacheFolder(name: String) = getFolder(cacheDir, name)

private const val BUFFER = 1024

internal fun Context.createZipFile(filePaths: List<String>, zipFileName: String): Uri? = try {
    if (filePaths.isEmpty()) {
        null
    } else {
        var origin: BufferedInputStream?
        val zipFile = createBugReportsFile(zipFileName)
        val output = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        val data = ByteArray(BUFFER)
        filePaths.forEach { path ->
            val input = FileInputStream(File(path))
            origin = BufferedInputStream(input, BUFFER)
            val entry = ZipEntry(path.substring(path.lastIndexOf("/") + 1))
            output.putNextEntry(entry)
            var count: Int? = null
            while (origin?.read(data, 0, BUFFER)?.also { count = it } != -1) {
                count?.let { output.write(data, 0, it) }
            }
            origin?.close()
        }
        output.close()
        getUriForFile(zipFile)
    }
} catch (e: Exception) {
    null
}

internal fun Context.text(text: Text) = when (text) {
    is Text.CharSequence -> text.charSequence
    is Text.ResourceId -> getText(text.resourceId)
}.append(text.suffix)

internal fun Context.getMediaFilePaths(ids: List<String>) = ids
    .map { fileName -> getUriForFile(getScreenCapturesFolder().resolve(fileName)) }
    .toPaths(getScreenCapturesFolder())

internal suspend fun Context.getCrashLogFilePaths(ids: List<String>) =
    BeagleCore.implementation.getCrashLogEntriesInternal().let { allCrashLogEntries ->
        ids.mapNotNull { id ->
            allCrashLogEntries.firstOrNull { it.id == id }?.let { entry ->
                createLogFile(
                    fileName = "${BeagleCore.implementation.behavior.bugReportingBehavior.getCrashLogFileName(currentTimestamp, entry.id)}.txt",
                    content = entry.getFormattedContents(BeagleCore.implementation.appearance.logShortTimestampFormatter).toString()
                )
            }
        }.toPaths(getLogsFolder())
    }

internal suspend fun Context.getNetworkLogFilePaths(ids: List<String>) =
    BeagleCore.implementation.getNetworkLogEntriesInternal().let { allNetworkLogEntries ->
        ids.mapNotNull { id ->
            allNetworkLogEntries.firstOrNull { it.id == id }?.let { entry ->
                createLogFile(
                    fileName = "${BeagleCore.implementation.behavior.networkLogBehavior.getFileName(currentTimestamp, entry.id)}.txt",
                    content = NetworkLogDetailDialogViewModel.createLogFileContents(
                        title = NetworkLogDetailDialogViewModel.createTitle(
                            isOutgoing = entry.isOutgoing,
                            url = entry.url
                        ),
                        metadata = NetworkLogDetailDialogViewModel.createMetadata(
                            context = this,
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
        }.toPaths(getLogsFolder())
    }

internal suspend fun Context.getLogFilePaths(ids: List<String>) =
    BeagleCore.implementation.getLogEntriesInternal(null).filter { ids.contains(it.id) }.mapNotNull { entry ->
        createLogFile(
            fileName = "${BeagleCore.implementation.behavior.logBehavior.getFileName(currentTimestamp, entry.id)}.txt",
            content = entry.getFormattedContents(BeagleCore.implementation.appearance.logShortTimestampFormatter).toString()
        )
    }.toPaths(getLogsFolder())

internal suspend fun Context.getLifecycleLogFilePaths(ids: List<String>) =
    BeagleCore.implementation.getLifecycleLogEntriesInternal(null).let { allLifecycleLogEntries ->
        ids.mapNotNull { id ->
            allLifecycleLogEntries.firstOrNull { it.id == id }?.let { entry ->
                createLogFile(
                    fileName = "${BeagleCore.implementation.behavior.lifecycleLogBehavior.getFileName(currentTimestamp, entry.id)}.txt",
                    content = text(
                        LifecycleLogListDelegate.format(
                            entry = entry,
                            formatter = BeagleCore.implementation.appearance.logShortTimestampFormatter,
                            shouldDisplayFullNames = BeagleCore.implementation.behavior.lifecycleLogBehavior.shouldDisplayFullNames
                        )
                    ).toString()
                )
            }
        }.toPaths(getLogsFolder())
    }