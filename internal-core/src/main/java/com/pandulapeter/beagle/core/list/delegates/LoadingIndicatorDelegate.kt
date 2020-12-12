package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.LoadingIndicatorCell
import com.pandulapeter.beagle.modules.LoadingIndicatorModule

internal class LoadingIndicatorDelegate : Module.Delegate<LoadingIndicatorModule> {

    override fun createCells(module: LoadingIndicatorModule): List<Cell<*>> = listOf(LoadingIndicatorCell(id = module.id))
}