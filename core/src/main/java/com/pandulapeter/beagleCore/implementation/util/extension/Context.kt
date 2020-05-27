package com.pandulapeter.beagleCore.implementation.util.extension

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

internal fun Context.registerSensorEventListener(sensorEventListener: SensorEventListener) = (getSystemService(Context.SENSOR_SERVICE) as? SensorManager?)?.run {
    registerListener(sensorEventListener, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
} ?: false