package com.pandulapeter.beagle.core.util.extension

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.ContextThemeWrapper
import com.pandulapeter.beagle.BeagleCore

internal fun Context.registerSensorEventListener(sensorEventListener: SensorEventListener) = (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
    registerListener(sensorEventListener, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
} ?: false

internal val Context.themedContext get() = (BeagleCore.implementation.appearance.themeResourceId?.let { ContextThemeWrapper(this, it) } ?: this)