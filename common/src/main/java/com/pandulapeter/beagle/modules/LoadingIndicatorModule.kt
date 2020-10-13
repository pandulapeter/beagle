package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.commonBase.randomId

/**
 * Displays a circular progress bar. Useful for temporary placeholders during async operations.
 *
 * You might want to provide a constant ID so that you can manually remove the module later.
 *
 * @param id - A unique identifier for the module. [randomId] by default.
 */
@Suppress("unused")
data class LoadingIndicatorModule(
    override val id: String = randomId
) : Module<LoadingIndicatorModule>