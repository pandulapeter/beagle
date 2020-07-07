package com.pandulapeter.beagle.core.util.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import com.pandulapeter.beagle.BeagleCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal fun Context.color(@ColorRes colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

@SuppressLint("ResourceAsColor")
@ColorInt
internal fun Context.colorResource(@AttrRes id: Int): Int {
    val resolvedAttr = TypedValue()
    theme.resolveAttribute(id, resolvedAttr, true)
    return color(resolvedAttr.run { if (resourceId != 0) resourceId else data })
}

internal fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

internal fun Context.drawable(@DrawableRes drawableResourceId: Int) = AppCompatResources.getDrawable(this, drawableResourceId)

internal fun Context.tintedDrawable(@DrawableRes drawableResourceId: Int, @ColorInt tint: Int) = drawable(drawableResourceId)!!.let { drawable ->
    DrawableCompat.wrap(drawable.mutate()).apply {
        DrawableCompat.setTint(this, tint)
        DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
    }
}

internal fun Context.registerSensorEventListener(sensorEventListener: SensorEventListener) = (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
    registerListener(sensorEventListener, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
} ?: false

internal fun Context.unregisterSensorEventListener(sensorEventListener: SensorEventListener) {
    (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.unregisterListener(sensorEventListener)
}

fun Context.applyTheme() = BeagleCore.implementation.appearance.themeResourceId?.let { ContextThemeWrapper(this, it) } ?: this

internal fun Context.getUriForFile(file: File) = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".beagle.fileProvider", file)

internal suspend fun Context.createScreenshotFromBitmap(bitmap: Bitmap, fileName: String): Uri? = withContext(Dispatchers.IO) {
    val folder = File(cacheDir, "beagleScreenCaptures")
    try {
        folder.mkdirs()
        val file = File(folder, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        getUriForFile(file)
    } catch (_: IOException) {
        null
    }
}