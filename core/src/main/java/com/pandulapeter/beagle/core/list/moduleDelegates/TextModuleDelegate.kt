package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ModuleDelegate
import com.pandulapeter.beagle.modules.TextModule
import com.pandulapeter.beagle.core.list.cells.TextCell

internal class TextModuleDelegate : ModuleDelegate<TextModule> {

    override fun createCells(module: TextModule): List<Cell<*>> = listOf(
        TextCell(
            id = module.id,
            text = module.text,
            color = module.color
        )
    )
}