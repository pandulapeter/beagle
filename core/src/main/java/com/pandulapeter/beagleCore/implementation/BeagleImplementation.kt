package com.pandulapeter.beagleCore.implementation

import android.app.Application
import androidx.annotation.RestrictTo
import com.pandulapeter.shared.configuration.Appearance
import com.pandulapeter.shared.configuration.Behavior
import com.pandulapeter.shared.contracts.BeagleContract
import com.pandulapeter.beagleCore.implementation.manager.FragmentManagerProvider
import com.pandulapeter.beagleCore.implementation.manager.ShakeDetector
import com.pandulapeter.beagleCore.implementation.manager.UiManagerContract
import com.pandulapeter.beagleCore.implementation.util.extension.registerSensorEventListener

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