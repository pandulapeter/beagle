package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Displays a circular progress bar. Useful for temporary placeholders during async operations.
 *
 * You might want to provide a constant ID so that you can manually remove the module later.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 */
data class LoadingIndicatorModule(
    override val id: String = UUID.randomUUID().toString()
) : Module<LoadingIndicatorModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}