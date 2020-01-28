package com.pandulapeter.beagleCore.contracts

import android.app.Activity
import android.app.Application
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick

/**
 * This interface assures that the real implementation and the "noop" variant have the same public API. See one of those for the function documentations.
 */
@Suppress("unused")
interface BeagleContract {

    var isEnabled: Boolean

    fun imprint(application: Application, appearance: Appearance = Appearance())

    fun learn(vararg tricks: Trick)

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Use Beagle.learn(vararg modules) function instead.")
    fun learn(tricks: List<Trick>) = learn(*Array(tricks.size) { tricks[it] })

    fun learn(trick: Trick, positioning: Positioning = Positioning.Bottom)

    fun forget(id: String)

    fun fetch(activity: Activity)

    fun dismiss(activity: Activity): Boolean

    fun log(message: String, tag: String? = null, payload: String? = null)

    /**
     * API for serious people.
     */

    fun initialize(application: Application, appearance: Appearance = Appearance()) = imprint(application, appearance)

    fun setModules(vararg modules: Trick) = learn(*modules)

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Use Beagle.setModules(vararg modules) function instead.")
    fun setModules(modules: List<Trick>) = learn(*Array(modules.size) { modules[it] })

    fun putModule(module: Trick, positioning: Positioning = Positioning.Bottom) = learn(module, positioning)

    fun removeModule(id: String) = forget(id)

    fun openDrawer(activity: Activity) = fetch(activity)

    fun closeDrawer(activity: Activity) = dismiss(activity)
}