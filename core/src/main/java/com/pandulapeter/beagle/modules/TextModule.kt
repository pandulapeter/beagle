package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.builtIn.TextModuleContract
import com.pandulapeter.beagle.core.list.cells.TextCell
import java.util.UUID

/**
 * Displays a piece of text.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param text - The text to display.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 */
open class TextModule(
    override val id: String = "text_${UUID.randomUUID()}",
    override val text: CharSequence,
    @ColorInt override val color: Int? = null
) : TextModuleContract {

    final override fun createCells() = listOf<Cell<*>>(TextCell(id, text, color))
}