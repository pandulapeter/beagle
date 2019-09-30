package com.pandulapeter.beagle

import android.app.Activity
import android.app.Application
import com.pandulapeter.beagleCore.ModulePositioning
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.contracts.BeagleModuleContract
import com.pandulapeter.beagleCore.contracts.BeagleContract

/**
 * Fake implementation to be used in release builds.
 */
object Beagle : BeagleContract {

    /**
     * Does nothing.
     */
    override var isEnabled = false
        set(_) = Unit

    /**
     * Does nothing.
     */
    override fun imprint(application: Application, appearance: Appearance) = Unit

    /**
     * Does nothing.
     */
    override fun learn(modules: List<BeagleModuleContract>) = Unit

    /**
     * Does nothing.
     */
    override fun learn(module: BeagleModuleContract, positioning: ModulePositioning) = Unit

    /**
     * Does nothing.
     */
    override fun forget(id: String) = Unit

    /**
     * Does nothing and returns false.
     */
    override fun fetch(activity: Activity) = false

    /**
     * Does nothing.
     */
    override fun dismiss(activity: Activity) = Unit

    /**
     * Does nothing.
     */
    override fun log(message: String, tag: String?, payload: String?) = Unit
}