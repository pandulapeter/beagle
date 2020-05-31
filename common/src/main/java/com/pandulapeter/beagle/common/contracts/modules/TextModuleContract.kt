package com.pandulapeter.beagle.common.contracts.modules

import com.pandulapeter.beagle.common.contracts.Module

/**
 * This interface ensures that the real implementation and the noop variant have the same public API.
 *
 * A TextModule displays a [CharSequence].
 */
interface TextModuleContract : Module {

    /**
     * The text to display.
     */
    val text: CharSequence
}