package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.CheckBoxCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.MultipleSelectionListModule

internal class MultipleSelectionListDelegate : ExpandableModuleDelegate<MultipleSelectionListModule>, PersistableModuleDelegate.StringSet<MultipleSelectionListModule>() {

    override fun MutableList<Cell<*>>.addItems(module: MultipleSelectionListModule) {
        addAll(module.items.map { item ->
            CheckBoxCell(
                id = "${module.id}_${item.id}",
                text = item.text,
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
}