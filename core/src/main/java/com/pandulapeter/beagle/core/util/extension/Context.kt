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
import com.pandulapeter.beagle.core.util.getFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException

internal fun Context.registerSensorEventListener(sensorEventListener: SensorEventListener) = (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
    registerListener(sensorEventListener, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
} ?: false

internal fun Context.unregisterSensorEventListener(sensorEventListener: SensorEventListener) {
    (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.unregisterListener(sensorEventListener)
}

fun Context.applyTheme() = BeagleCore.implementation.appearance.themeResourceId?.let { ContextThemeWrapper(this, it) } ?: this

internal fun Context.getUriForFile(file: File) = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".beagle.fileProvider", file)

@Suppress("BlockingMethodInNonBlockingContext")
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

internal fun Context.getLogsFolder() = getCacheFolder(LOGS_FOLDER_NAME)

internal fun Context.createLogFile(fileName: String) = File(getLogsFolder(), fileName)

@Suppress("BlockingMethodInNonBlockingContext")
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

@Suppress("BlockingMethodInNonBlockingContext")
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

internal fun Context.text(text: Text) = when (text) {
    is Text.CharSequence -> text.charSequence
    is Text.ResourceId -> getText(text.resourceId)
}.append(text.suffix)