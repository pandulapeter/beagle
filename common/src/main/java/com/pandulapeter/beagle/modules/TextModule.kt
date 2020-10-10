package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

/**
 * Displays a piece of text. Can be used for click handling as well.
 *
 * @param text - The text to display.
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param onItemSelected - Callback called when the user clicks on the text. Optional, null by default.
 */
data class TextModule(
    val text: Text,
    override val id: String = UUID.randomUUID().toString(),
    val onItemSelected: (() -> Unit)? = null
) : Module<TextModule> {

    constructor(
        text: CharSequence,
        id: String = UUID.randomUUID().toString(),
        onItemSelected: (() -> Unit)? = null
    ) : this(
        text = Text.CharSequence(text),
        id = id,
        onItemSelected = onItemSelected
    )

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}