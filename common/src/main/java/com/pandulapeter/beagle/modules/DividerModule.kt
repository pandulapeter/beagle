package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Displays a simple horizontal line.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 */
data class DividerModule(
    override val id: String = UUID.randomUUID().toString()
) : Module<DividerModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}