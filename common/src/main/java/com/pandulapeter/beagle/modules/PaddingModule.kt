package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.commonBase.randomId

/**
 * Adds empty space between two other modules.
 *
 * @param id - A unique identifier for the module. [randomId] by default.
 */
@Suppress("unused")
data class PaddingModule(
    override val id: String = randomId
) : Module<PaddingModule>