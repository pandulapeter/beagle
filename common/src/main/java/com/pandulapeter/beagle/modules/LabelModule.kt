package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Displays a section header label.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param text - The text to display.
 */
data class LabelModule(
    override val id: String = UUID.randomUUID().toString(),
    val text: CharSequence
) : Module<LabelModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}