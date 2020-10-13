package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * Adds empty space between two other modules.
 *
 * @param id - A unique identifier for the module. [BeagleContract.randomId] by default.
 */
@Suppress("unused")
data class PaddingModule(
    override val id: String = BeagleContract.randomId
) : Module<PaddingModule>