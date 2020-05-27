package com.pandulapeter.beagle.implementation

import android.app.Application
import com.pandulapeter.beagle.implementation.manager.FragmentManagerProvider
import com.pandulapeter.beagle.implementation.manager.ShakeDetector
import com.pandulapeter.beagle.implementation.ui.BeagleDebugMenu
import com.pandulapeter.beagle.implementation.util.consume
import com.pandulapeter.beagle.implementation.util.extension.registerSensorEventListener
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Behavior
import com.pandulapeter.beagleCore.contracts.BeagleContract

internal class BeagleImplementation : BeagleContract {

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

    override fun show() = lifecycleInjector.fragmentManager?.let { consume { BeagleDebugMenu.show(it) } } ?: false

    override fun hide() = (lifecycleInjector.fragmentManager?.findFragmentByTag(BeagleDebugMenu.TAG) as? BeagleDebugMenu?)?.let { consume { it.dismiss() } } ?: false
}