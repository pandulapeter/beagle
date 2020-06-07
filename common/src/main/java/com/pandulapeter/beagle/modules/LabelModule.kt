package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Displays a section header label.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param title - The text to display on the label.
 */
data class LabelModule(
    override val id: String = UUID.randomUUID().toString(),
    val title: CharSequence
) : Module<LabelModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}