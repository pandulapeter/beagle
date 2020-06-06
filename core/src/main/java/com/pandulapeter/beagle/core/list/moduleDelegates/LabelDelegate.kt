package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.LabelCell
import com.pandulapeter.beagle.modules.LabelModule

internal class LabelDelegate : Module.Delegate<LabelModule> {

    override fun createCells(module: LabelModule): List<Cell<*>> = listOf(
        LabelCell(
            id = module.id,
            text = module.text,
            color = module.color
        )
    )
}