package com.pandulapeter.beagle.common.contracts.module.builtIn

import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
interface TextModuleContract : Module {

    val text: CharSequence
    val color: Int?
}