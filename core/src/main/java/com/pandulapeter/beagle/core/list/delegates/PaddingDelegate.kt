package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.PaddingCell
import com.pandulapeter.beagle.modules.PaddingModule

internal class PaddingDelegate : Module.Delegate<PaddingModule> {

    override fun createCells(module: PaddingModule): List<Cell<*>> = listOf(
        PaddingCell(
            id = module.id,
            size = module.size
        )
    )
}