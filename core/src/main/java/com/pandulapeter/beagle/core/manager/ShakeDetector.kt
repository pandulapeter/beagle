package com.pandulapeter.beagle.core.manager

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.registerSensorEventListener
import com.pandulapeter.beagle.core.util.extension.unregisterSensorEventListener
import kotlin.math.abs

internal class ShakeDetector(
    private val onShakeDetected: () -> Unit
) : SensorEventListener, LifecycleObserver {

    private var lastSensorUpdate = 0L
    private val previousEvent = SensorValues()
    private var application: Application? = null

    fun initialize(application: Application): Boolean {
        application.unregisterSensorEventListener(this)
        return application.registerSensorEventListener(this).also {
            if (it) {
                this.application = application
                ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerSensor() = application?.registerSensorEventListener(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterSensor() = application?.unregisterSensorEventListener(this)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        if (BeagleCore.implementation.currentActivity?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == true) {
            BeagleCore.implementation.behavior.shakeThreshold?.let { threshold ->
                if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastSensorUpdate > SHAKE_DETECTION_DELAY) {
                        if (abs(event.x + event.y + event.z - previousEvent.x - previousEvent.y - previousEvent.z) / (currentTime - lastSensorUpdate) * 100 > threshold) {
                            onShakeDetected()
                        }
                        previousEvent.x = event.x
                        previousEvent.y = event.y
                        previousEvent.z = event.z
                        lastSensorUpdate = currentTime
                    }
                }
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
    }
}
