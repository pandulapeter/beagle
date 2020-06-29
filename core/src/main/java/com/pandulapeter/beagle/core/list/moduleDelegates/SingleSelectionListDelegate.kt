package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.RadioButtonCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.SingleSelectionListModule

internal class SingleSelectionListDelegate<T : BeagleListItemContract> : ExpandableModuleDelegate<SingleSelectionListModule<T>>, PersistableModuleDelegate.String<SingleSelectionListModule<T>>() {

    override fun canExpand(module: SingleSelectionListModule<T>) = module.items.isNotEmpty()

    override fun MutableList<Cell<*>>.addItems(module: SingleSelectionListModule<T>) {
        addAll(module.items.map { item ->
            RadioButtonCell(
                id = "${module.id}_${item.id}",
                text = item.title,
                isChecked = item.id == getCurrentValue(module),
                onValueChanged = { setCurrentValue(module, item.id) }
            )
        })
    }

    override fun createCells(module: SingleSelectionListModule<T>) = super.createCells(module).also {
        callListenerForTheFirstTimeIfNeeded(module, getCurrentValue(module))
    }
}