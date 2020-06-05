package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ModuleDelegate
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.ButtonModule

internal class ButtonModuleDelegate : ModuleDelegate<ButtonModule> {

    override fun createCells(module: ButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            color = module.color,
            onButtonPressed = module.onButtonPressed
        )
    )
}