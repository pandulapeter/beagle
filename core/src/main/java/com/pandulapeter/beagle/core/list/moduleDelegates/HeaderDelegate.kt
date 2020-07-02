package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.HeaderCell
import com.pandulapeter.beagle.modules.HeaderModule

internal class HeaderDelegate : Module.Delegate<HeaderModule> {

    override fun createCells(module: HeaderModule): List<Cell<*>> = listOf(
        HeaderCell(
            id = module.id,
            title = module.title,
            subtitle = module.subtitle,
            text = module.text
        )
    )
}