package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.CheckBoxCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.MultipleSelectionListModule

internal class MultipleSelectionListDelegate<T : BeagleListItemContract> : ExpandableModuleDelegate<MultipleSelectionListModule<T>>,
    PersistableModuleDelegate.StringSet<MultipleSelectionListModule<T>>() {

    override fun MutableList<Cell<*>>.addItems(module: MultipleSelectionListModule<T>) {
        addAll(module.items.map { item ->
            CheckBoxCell(
                id = "${module.id}_${item.id}",
                text = item.name,
                isChecked = getCurrentValue(module).contains(item.id),
                onValueChanged = { isChecked ->
                    if (isChecked) {
                        setCurrentValue(module, getCurrentValue(module) + item.id)
                    } else {
                        setCurrentValue(module, getCurrentValue(module) - item.id)
                    }
                }
            )
        })
    }

    override fun createCells(module: MultipleSelectionListModule<T>) = super.createCells(module).also {
        callListenerForTheFirstTimeIfNeeded(module, getCurrentValue(module))
    }
}