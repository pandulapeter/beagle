package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.DividerCell
import com.pandulapeter.beagle.modules.DividerModule

internal class DividerDelegate : Module.Delegate<DividerModule> {

    override fun createCells(module: DividerModule): List<Cell<*>> = listOf(DividerCell(id = module.id))
}