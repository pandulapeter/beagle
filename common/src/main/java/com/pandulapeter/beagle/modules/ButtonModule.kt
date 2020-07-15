package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID


/**
 * Displays a simple button.
 *
 * @param text - The text to display on the button.
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param onButtonPressed - Callback called when the user presses the button.
 */
data class ButtonModule(
    val text: CharSequence,
    override val id: String = UUID.randomUUID().toString(),
    val onButtonPressed: () -> Unit
) : Module<ButtonModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}