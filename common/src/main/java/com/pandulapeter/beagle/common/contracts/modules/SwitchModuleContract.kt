package com.pandulapeter.beagle.common.contracts.modules

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
interface SwitchModuleContract : TextModuleContract {

    val initialValue: Boolean
    val onValueChanged: (Boolean) -> Unit
}