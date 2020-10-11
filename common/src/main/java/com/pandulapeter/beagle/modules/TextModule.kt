package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule.Companion.DEFAULT_ON_ITEM_SELECTED
import java.util.UUID

/**
 * Displays a piece of text. Can be used for click handling as well.
 *
 * @param text - The text to display.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 * @param onItemSelected - Callback called when the user clicks on the text, or null to disable selection. [DEFAULT_ON_ITEM_SELECTED] by default.
 */
data class TextModule(
    val text: Text,
    override val id: String = Module.randomId,
    val onItemSelected: (() -> Unit)? = DEFAULT_ON_ITEM_SELECTED
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

    companion object {
        private val DEFAULT_ON_ITEM_SELECTED: (() -> Unit)? = null
    }
}