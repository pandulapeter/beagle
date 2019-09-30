package com.pandulapeter.beagleCore.contracts

import android.app.Activity
import android.app.Application
import com.pandulapeter.beagleCore.ModulePositioning
import com.pandulapeter.beagleCore.configuration.Appearance

/**
 * This interface assures that the real implementation and the "noop" variant have the same public API. See one of those for the function documentations.
 */
interface BeagleContract {

    var isEnabled: Boolean

    fun imprint(application: Application, appearance: Appearance = Appearance())

    fun learn(modules: List<BeagleModuleContract>)

    fun learn(module: BeagleModuleContract, positioning: ModulePositioning = ModulePositioning.Bottom)

    fun forget(id: String)

    fun fetch(activity: Activity): Boolean

    fun dismiss(activity: Activity)

    fun log(message: String, tag: String? = null, payload: String? = null)
}