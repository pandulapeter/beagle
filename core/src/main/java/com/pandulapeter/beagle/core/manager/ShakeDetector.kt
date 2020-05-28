package com.pandulapeter.beagle.core.manager

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import kotlin.math.abs

internal class ShakeDetector(private val onShakeDetected: () -> Unit) : SensorEventListener {

    private var lastSensorUpdate = 0L
    private val lastSensorValues = SensorValues()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastSensorUpdate > SHAKE_DETECTION_DELAY) {
                if (abs(event.x + event.y + event.z - lastSensorValues.x - lastSensorValues.y - lastSensorValues.z) / ((currentTime - lastSensorUpdate) * 10000) > SHAKE_THRESHOLD) {
                    onShakeDetected()
                }
                lastSensorValues.x = event.x
                lastSensorValues.y = event.y
                lastSensorValues.z = event.z
                lastSensorUpdate = currentTime
            }
        }
    }

    private data class SensorValues(
        var x: Float = 0f,
        var y: Float = 0f,
        var z: Float = 0f
    )

    private val SensorEvent.x get() = values[0]
    private val SensorEvent.y get() = values[1]
    private val SensorEvent.z get() = values[2]

    companion object {
        private const val SHAKE_DETECTION_DELAY = 100L
        private const val SHAKE_THRESHOLD = 1200
    }
}
