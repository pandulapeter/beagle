package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.ModuleCell
import com.pandulapeter.beagle.common.contracts.modules.TextModuleContract

/**
 * See [TextModuleContract] for documentation.
 */
data class TextModule(
    override val id: String = "",
    override val text: CharSequence = "",
    @ColorInt override val color: Int? = null
) : TextModuleContract {

    override fun createCells() = emptyList<ModuleCell<*>>()
}