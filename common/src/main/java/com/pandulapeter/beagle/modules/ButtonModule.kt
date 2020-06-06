package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID


/**
 * Displays a simple button.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param text - The text to display on the button.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param onButtonPressed - Callback called when the user presses the button.
 */
data class ButtonModule(
    override val id: String = UUID.randomUUID().toString(),
    val text: CharSequence,
    @ColorInt val color: Int? = null,
    val onButtonPressed: () -> Unit
) : Module<ButtonModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}