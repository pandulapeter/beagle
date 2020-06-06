package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.RadioButtonCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.SingleSelectionListModule

internal class SingleSelectionListDelegate : ExpandableModuleDelegate<SingleSelectionListModule>, PersistableModuleDelegate.String<SingleSelectionListModule>() {

    override fun MutableList<Cell<*>>.addItems(module: SingleSelectionListModule) {
        addAll(module.items.map { item ->
            RadioButtonCell(
                id = "${module.id}_${item.id}",
                color = module.color,
                text = item.text,
                isChecked = item.id == getCurrentValue(module),
                onValueChanged = { setCurrentValue(module, item.id) }
            )
        })
    }
}