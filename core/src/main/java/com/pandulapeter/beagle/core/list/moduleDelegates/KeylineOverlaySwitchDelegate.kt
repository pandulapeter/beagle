package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule

internal class KeylineOverlaySwitchDelegate : PersistableModuleDelegate.Boolean<KeylineOverlaySwitchModule>() {

    override fun createCells(module: KeylineOverlaySwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = module.text,
            isChecked = getCurrentValue(module),
            onValueChanged = { newValue -> setCurrentValue(module, newValue) })
    )

    override fun callOnValueChanged(module: KeylineOverlaySwitchModule, newValue: kotlin.Boolean) {
        //TODO
        module.onValueChanged(newValue)
    }
}