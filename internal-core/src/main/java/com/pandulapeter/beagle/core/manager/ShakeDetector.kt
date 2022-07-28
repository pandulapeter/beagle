package com.pandulapeter.beagle.core.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.core.util.extension.registerSensorEventListener
import com.pandulapeter.beagle.core.util.extension.unregisterSensorEventListener
import kotlin.math.abs

@Suppress("unused")
internal class ShakeDetector : SensorEventListener, DefaultLifecycleObserver {

    private var lastSensorUpdate = 0L
    private val previousEvent = SensorValues()
    private var application: Application? = null
    private val vibrator by lazy { application?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator? }

    fun initialize(application: Application): Boolean {
        application.unregisterSensorEventListener(this)
        return application.registerSensorEventListener(this).also {
            if (it) {
                this.application = application
                ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        application?.registerSensorEventListener(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        application?.unregisterSensorEventListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        if (BeagleCore.implementation.currentActivity?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == true
            && BeagleCore.implementation.currentActivity?.let { BeagleCore.implementation.behavior.shouldShowDebugMenu.invoke(it) } == true) {
            BeagleCore.implementation.behavior.shakeDetectionBehavior.threshold?.let { threshold ->
                if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val currentTime = currentTimestamp
                    if (currentTime - lastSensorUpdate > SHAKE_DETECTION_DELAY) {
                        if (abs(event.x + event.y + event.z - previousEvent.x - previousEvent.y - previousEvent.z) / (currentTime - lastSensorUpdate) * 100 > threshold) {
                            showDebugMenuAndVibrateIfNeeded()
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


    @SuppressLint("MissingPermission")
    private fun showDebugMenuAndVibrateIfNeeded() {
        if (BeagleCore.implementation.show()) {
            vibrator?.run {
                val duration = BeagleCore.implementation.behavior.shakeDetectionBehavior.hapticFeedbackDuration
                if (duration > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrate(duration)
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
