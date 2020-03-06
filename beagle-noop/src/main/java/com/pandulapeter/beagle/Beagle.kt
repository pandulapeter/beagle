package com.pandulapeter.beagle

import android.app.Activity
import com.pandulapeter.beagleCore.contracts.BeagleContract

/**
 * Fake implementation to be used in release builds.
 */
object Beagle : BeagleContract {

    /**
     * Does nothing (Beagle is always disabled in the noop variant).
     */
    override var isEnabled = false

    /**
     * Returns null (Beagle does not work at all in the noop variant).
     */
    override val currentActivity: Activity? = null
}