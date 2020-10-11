package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * Displays a simple horizontal line.
 *
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 */
data class DividerModule(
    override val id: String = Module.randomId
) : Module<DividerModule>