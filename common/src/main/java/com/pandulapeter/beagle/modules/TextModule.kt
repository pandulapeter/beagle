package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Displays a piece of text. Can be used for click handling as well.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param text - The text to display.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param onItemSelected - Callback called when the user clicks on the text. Optional, null by default.
 */
data class TextModule(
    override val id: String = "text_${UUID.randomUUID()}",
    val text: CharSequence,
    @ColorInt val color: Int? = null,
    val onItemSelected: (() -> Unit)? = null
) : Module<TextModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}