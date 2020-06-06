package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.ItemListModule

internal class ItemListDelegate : ExpandableModuleDelegate<ItemListModule> {

    override fun MutableList<Cell<*>>.addItems(module: ItemListModule) {
        addAll(module.items.map { item ->
            TextCell(
                id = "${module.id}_${item.id}",
                color = module.color,
                text = item.text,
                onItemSelected = module.onItemSelected?.let { onItemSelected -> { onItemSelected(item.id) } }
            )
        })
    }
}