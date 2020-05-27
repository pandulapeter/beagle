package com.pandulapeter.beagle.core.implementation

import android.app.Application
import androidx.annotation.RestrictTo
import com.pandulapeter.beagle.shared.configuration.Appearance
import com.pandulapeter.beagle.shared.configuration.Behavior
import com.pandulapeter.beagle.shared.contracts.BeagleContract
import com.pandulapeter.beagle.core.implementation.manager.FragmentManagerProvider
import com.pandulapeter.beagle.core.implementation.manager.ShakeDetector
import com.pandulapeter.beagle.core.implementation.manager.UiManagerContract
import com.pandulapeter.beagle.core.implementation.util.extension.registerSensorEventListener

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