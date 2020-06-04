package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.builtIn.ButtonModuleContract
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import java.util.UUID

/**
 * Displays a simple button.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param text - The text to display on the button.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param onButtonPressed - Callback called when the user presses the button.
 */
open class ButtonModule(
    override val id: String = "text_${UUID.randomUUID()}",
    override val text: CharSequence,
    @ColorInt override val color: Int? = null,
    override val onButtonPressed: () -> Unit
) : ButtonModuleContract {

    final override fun createCells() = listOf<Cell<*>>(ButtonCell(id, text, color, onButtonPressed))
}