package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.ItemListModule

internal class ItemListDelegate<T : BeagleListItemContract> : ExpandableModuleDelegate<ItemListModule<T>> {

    override fun canExpand(module: ItemListModule<T>) = module.items.isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: ItemListModule<T>) {
        addAll(module.items.map { item ->
            ExpandedItemTextCell(
                id = "${module.id}_${item.id}",
                text = item.title,
                isEnabled = true,
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