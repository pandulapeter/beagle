package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.PersistableModule
import com.pandulapeter.beagle.core.list.cells.CheckboxCell
import com.pandulapeter.beagle.modules.CheckboxModule

internal class CheckboxDelegate : PersistableModule.Delegate<Boolean, CheckboxModule>() {

    override fun getCurrentValue(module: CheckboxModule): Boolean = if (module.shouldBePersisted) {
        BeagleCore.implementation.localStorageManager.booelans[module.id] ?: module.initialValue
    } else {
        BeagleCore.implementation.memoryStorageManager.booleans[module.id] ?: module.initialValue
    }

    override fun setCurrentValue(module: CheckboxModule, newValue: Boolean) {
        if (newValue != getCurrentValue(module)) {
            if (module.shouldBePersisted) {
                BeagleCore.implementation.localStorageManager.booelans[module.id] = newValue
            } else {
                BeagleCore.implementation.memoryStorageManager.booleans[module.id] = newValue
            }
            BeagleCore.implementation.updateCells()
            module.onValueChanged(newValue)
        }
    }

    override fun createCells(module: CheckboxModule): List<Cell<*>> = listOf(
        CheckboxCell(
            id = module.id,
            text = module.text,
            color = module.color,
            isChecked = getCurrentValue(module),
            onValueChanged = { setCurrentValue(module, it) })
    )
}