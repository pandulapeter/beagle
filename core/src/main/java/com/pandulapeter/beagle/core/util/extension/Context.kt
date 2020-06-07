package com.pandulapeter.beagle.core.util.extension

import android.content.Context
import android.graphics.PorterDuff
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.ContextThemeWrapper
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.pandulapeter.beagle.BeagleCore

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