package com.pandulapeter.beagle.core

import android.app.Application
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.VisibilityListener
import com.pandulapeter.beagle.core.manager.CurrentActivityProvider
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.manager.VisibilityListenerManager
import com.pandulapeter.beagle.core.util.extension.hideKeyboard
import com.pandulapeter.beagle.core.util.extension.registerSensorEventListener

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
    private val shakeDetector by lazy { ShakeDetector({ show() }) }
    private val currentActivityProvider by lazy { CurrentActivityProvider(uiManager) }
    private val visibilityListenerManager by lazy { VisibilityListenerManager() }

    init {
        BeagleCore.implementation = this
    }

    override fun initialize(
        application: Application,
        appearance: Appearance,
        behavior: Behavior
    ) = (behavior.shakeThreshold == null || application.registerSensorEventListener(shakeDetector)).also {
        this.appearance = appearance
        this.behavior = behavior
        currentActivityProvider.register(application)
    }

    override fun show() = (currentActivity?.let { uiManager.show(it) } ?: false)

    override fun hide() = (currentActivity?.let { uiManager.hide(it) } ?: false)

    override fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) = visibilityListenerManager.addVisibilityListener(listener, lifecycleOwner)

    override fun removeVisibilityListener(listener: VisibilityListener) = visibilityListenerManager.removeVisibilityListener(listener)

    override fun clearVisibilityListeners() = visibilityListenerManager.clearVisibilityListeners()

    fun notifyVisibilityListenersOnShow() = visibilityListenerManager.notifyVisibilityListenersOnShow()

    fun notifyVisibilityListenersOnHide() = visibilityListenerManager.notifyVisibilityListenersOnHide()

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit
}