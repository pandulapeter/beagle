package com.pandulapeter.beagle.common.contracts.module.builtIn

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
interface ButtonModuleContract : TextModuleContract {

    val onButtonPressed: () -> Unit
}