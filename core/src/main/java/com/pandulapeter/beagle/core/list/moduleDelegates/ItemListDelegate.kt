package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.ItemListModule

internal class ItemListDelegate<T : BeagleListItemContract> : ExpandableModuleDelegate<ItemListModule<T>> {

    override fun MutableList<Cell<*>>.addItems(module: ItemListModule<T>) {
        addAll(module.items.map { item ->
            TextCell(
                id = "${module.id}_${item.id}",
                text = item.title,
                onItemSelected = module.onItemSelected?.let { onItemSelected ->
                    {
                        module.items.firstOrNull { it.id == item.id }?.let { onItemSelected(it) }
                        Unit
                    }
                }
            )
        })
    }
}