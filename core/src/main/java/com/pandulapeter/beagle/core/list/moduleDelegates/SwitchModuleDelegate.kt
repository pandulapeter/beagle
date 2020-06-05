package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.PersistableModuleDelegate
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.modules.SwitchModule

internal class SwitchModuleDelegate : PersistableModuleDelegate<Boolean, SwitchModule> {
    
    override fun getCurrentValue(module: SwitchModule): Boolean = if (module.shouldBePersisted) {
        BeagleCore.implementation.sharedPreferencesManager.switchModules[module.id] ?: module.initialValue
    } else module.initialValue

    override fun setCurrentValue(module: SwitchModule, newValue: Boolean) {
        module.onValueChanged(newValue)
        if (module.shouldBePersisted) {
            BeagleCore.implementation.sharedPreferencesManager.switchModules[module.id] = newValue
        }
    }

    override fun createCells(module: SwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = module.text,
            color = module.color,
            isChecked = getCurrentValue(module),
            onValueChanged = { setCurrentValue(module, it) })
    )
}