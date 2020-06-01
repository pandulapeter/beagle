package com.pandulapeter.beagle.common.contracts.modules

import com.pandulapeter.beagle.common.contracts.Module

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 */
interface TextModuleContract : Module {

    val text: CharSequence
}