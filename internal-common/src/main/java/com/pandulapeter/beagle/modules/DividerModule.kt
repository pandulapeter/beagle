package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.commonBase.randomId

/**
 * Displays a simple horizontal line.
 *
 * @param id - A unique identifier for the module. [randomId] by default.
 */
data class DividerModule(
    override val id: String = randomId
) : Module<DividerModule>