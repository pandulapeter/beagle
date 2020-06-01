package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.Cell
import com.pandulapeter.beagle.common.contracts.modules.SwitchModuleContract

/**
 * Empty implementation.
 */
@Suppress("unused")
data class SwitchModule(
    override val id: String = "",
    override val text: CharSequence = "",
    @ColorInt override val color: Int? = null,
    override val initialValue: Boolean = false,
    override val onValueChanged: (Boolean) -> Unit = {}
) : SwitchModuleContract {

    override fun createCells() = emptyList<Cell<*>>()
}