package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.modules.HeaderModule

internal class HeaderDelegate : Module.Delegate<HeaderModule> {

    //TODO: Create a separate cell
    override fun createCells(module: HeaderModule): List<Cell<*>> = mutableListOf<Cell<*>>().apply {
        add(
            TextCell(
                id = "${module.id}_title",
                text = module.title,
                onItemSelected = null
            )
        )
        (module.subtitle)?.let { subtitle ->
            add(
                TextCell(
                    id = "${module.id}_subtitle",
                    text = subtitle,
                    onItemSelected = null
                )
            )
        }
        (module.text)?.let { text ->
            add(
                TextCell(
                    id = "${module.id}_text",
                    text = text,
                    onItemSelected = null
                )
            )
        }
    }
}