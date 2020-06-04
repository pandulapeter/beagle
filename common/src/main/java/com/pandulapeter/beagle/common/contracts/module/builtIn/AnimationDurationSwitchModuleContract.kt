package com.pandulapeter.beagle.common.contracts.module.builtIn

import androidx.annotation.RestrictTo

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
interface AnimationDurationSwitchModuleContract : SwitchModuleContract {

    val multiplier: Float

    companion object {
        const val ID = "animationDurationSwitch"
    }
}