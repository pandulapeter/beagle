package com.pandulapeter.beagle

import android.app.Activity
import android.app.Application
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick
import com.pandulapeter.beagleCore.configuration.TriggerGesture
import com.pandulapeter.beagleCore.contracts.BeagleContract
import com.pandulapeter.beagleCore.contracts.BeagleListener

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

    /**
     * Does nothing.
     */
    override fun imprint(application: Application, packageName: String?, triggerGesture: TriggerGesture, appearance: Appearance) = Unit

    /**
     * Does nothing.
     */
    override fun learn(vararg tricks: Trick) = Unit

    /**
     * Does nothing.
     */
    override fun learn(trick: Trick, positioning: Positioning) = Unit

    /**
     * Does nothing.
     */
    override fun forget(id: String) = Unit

    /**
     * Does nothing and returns false.
     */
    override fun dismiss(activity: Activity) = false

    /**
     * Does nothing and returns false.
     */
    override fun dismiss() = false

    /**
     * Does nothing.
     */
    override fun fetch(activity: Activity) = Unit

    /**
     * Does nothing.
     */
    override fun fetch() = Unit

    /**
     * Does nothing.
     */
    override fun log(message: String, tag: String?, payload: String?) = Unit

    /**
     * Does nothing.
     */
    override fun addListener(listener: BeagleListener) = Unit

    /**
     * Does nothing.
     */
    override fun removeListener(listener: BeagleListener) = Unit

    /**
     * Does nothing.
     */
    override fun removeAllListeners() = Unit
}