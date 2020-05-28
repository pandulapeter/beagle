package com.pandulapeter.beagle.core

import android.app.Application
import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.manager.CurrentActivityProvider
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.util.extension.hideKeyboard
import com.pandulapeter.beagle.core.util.extension.registerSensorEventListener
import com.pandulapeter.beagle.shared.configuration.Appearance
import com.pandulapeter.beagle.shared.configuration.Behavior
import com.pandulapeter.beagle.shared.contracts.BeagleContract
import com.pandulapeter.beagle.shared.contracts.VisibilityListener

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BeagleImplementation(private val uiManager: UiManagerContract) : BeagleContract {

    override var isUiEnabled = true
        set(value) {
            if (!value) {
                hide()
            }
            field = value
        }
    override val currentActivity get() = currentActivityProvider.currentActivity
    var appearance = Appearance()
        private set
    var behavior = Behavior()
        private set
    private val shakeDetector by lazy {
        ShakeDetector(
            getShakeThreshold = { this.behavior.shakeThreshold },
            onShakeDetected = { show() })
    }
    private val currentActivityProvider by lazy { CurrentActivityProvider(uiManager) }
    private val visibilityListeners = mutableListOf<VisibilityListener>()

    init {
        BeagleCore.implementation = this
    }

    override fun initialize(
        application: Application,
        appearance: Appearance,
        behavior: Behavior
    ): Boolean = (behavior.shakeThreshold == null || application.registerSensorEventListener(shakeDetector)).also {
        this.appearance = appearance
        this.behavior = behavior
        currentActivityProvider.register(application)
    }

    override fun show() = (currentActivity?.let { uiManager.show(it) } ?: false)

    override fun hide() = (currentActivity?.let { uiManager.hide(it) } ?: false)

    override fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) {

        fun addVisibilityListener(listener: VisibilityListener) {
            if (!visibilityListeners.contains(listener)) {
                visibilityListeners.add(listener)
            }
        }

        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onCreate() = addVisibilityListener(listener)

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onDestroy() {
                removeVisibilityListener(listener)
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: addVisibilityListener(listener)
    }

    override fun removeVisibilityListener(listener: VisibilityListener) {
        visibilityListeners.remove(listener)
    }

    override fun clearVisibilityListeners() = visibilityListeners.clear()

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit

    fun notifyVisibilityListenersOnShow() = visibilityListeners.forEach { it.onShown() }

    fun notifyVisibilityListenersOnHide() = visibilityListeners.forEach { it.onHidden() }
}