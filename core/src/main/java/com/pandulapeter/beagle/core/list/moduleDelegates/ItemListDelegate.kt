package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ExpandableHeaderCell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.modules.ItemListModule

internal class ItemListDelegate : Module.Delegate<ItemListModule>() {

    override fun createCells(module: ItemListModule): List<Cell<*>> = mutableListOf<Cell<*>>().apply {
        addHeader(module)
        addListItemsIfNeeded(module)
    }

    private fun MutableList<Cell<*>>.addHeader(module: ItemListModule) = add(
        ExpandableHeaderCell(
            id = module.id,
            text = module.title,
            color = module.color,
            isExpanded = module.isExpanded,
            onItemSelected = {
                module.isExpanded = !module.isExpanded
                BeagleCore.implementation.refreshCells()
            }
        )
    )

    private fun MutableList<Cell<*>>.addListItemsIfNeeded(module: ItemListModule) {
        if (module.isExpanded) {
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

    private var ItemListModule.isExpanded: Boolean
        get() = BeagleCore.implementation.memoryStorageManager.booleans[id] ?: isExpandedInitially
        set(value) {
            BeagleCore.implementation.memoryStorageManager.booleans[id] = value
        }
}