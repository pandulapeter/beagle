package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Adds empty space between two other modules.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 */
data class PaddingModule(
    override val id: String = UUID.randomUUID().toString()
) : Module<PaddingModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}