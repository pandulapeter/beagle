package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * Adds empty space between two other modules.
 *
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 */
data class PaddingModule(
    override val id: String = Module.randomId
) : Module<PaddingModule>