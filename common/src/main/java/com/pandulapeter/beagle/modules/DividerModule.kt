package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * Displays a simple horizontal line.
 *
 * @param id - A unique identifier for the module. [BeagleContract.randomId] by default.
 */
@Suppress("unused")
data class DividerModule(
    override val id: String = BeagleContract.randomId
) : Module<DividerModule>