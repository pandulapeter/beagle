package com.pandulapeter.beagle.core

import android.app.Application
import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.core.manager.FragmentManagerProvider
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.util.extension.registerSensorEventListener
import com.pandulapeter.beagle.shared.configuration.Appearance
import com.pandulapeter.beagle.shared.configuration.Behavior
import com.pandulapeter.beagle.shared.contracts.BeagleContract

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BeagleImplementation(private val uiManager: UiManagerContract) : BeagleContract {

    private var appearance = Appearance()
    private var behavior = Behavior()
    private val shakeDetector by lazy { ShakeDetector { show() } }
    private val lifecycleInjector by lazy { FragmentManagerProvider() }

    override fun initialize(
        application: Application,
        appearance: Appearance,
        behavior: Behavior
    ): Boolean = (!behavior.isShakeToOpenEnabled || application.registerSensorEventListener(shakeDetector)).also {
        this.appearance = appearance
        this.behavior = behavior
        lifecycleInjector.register(application)
    }

    override fun show() = lifecycleInjector.currentActivity?.let { uiManager.show(it) } ?: false

    override fun hide() = lifecycleInjector.currentActivity?.let { uiManager.hide(it) } ?: false
}