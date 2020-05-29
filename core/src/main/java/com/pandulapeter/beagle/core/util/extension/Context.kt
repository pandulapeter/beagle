package com.pandulapeter.beagle.core.util.extension

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

internal fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

internal fun Context.drawable(@DrawableRes drawableResourceId: Int) = AppCompatResources.getDrawable(this, drawableResourceId)

internal fun Context.registerSensorEventListener(sensorEventListener: SensorEventListener) = (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
    registerListener(sensorEventListener, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
} ?: false

internal fun Context.unregisterSensorEventListener(sensorEventListener: SensorEventListener) {
    (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.unregisterListener(sensorEventListener)
}