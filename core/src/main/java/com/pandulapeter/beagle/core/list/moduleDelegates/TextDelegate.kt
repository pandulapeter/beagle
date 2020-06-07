package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.modules.TextModule

internal class TextDelegate : Module.Delegate<TextModule> {

    override fun createCells(module: TextModule): List<Cell<*>> = listOf(
        TextCell(
            id = module.id,
            text = module.text,
            onItemSelected = module.onItemSelected
        )
    )
}