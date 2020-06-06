package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.SwitchModule

internal class SwitchDelegate : PersistableModuleDelegate.Boolean<SwitchModule>() {

    override fun createCells(module: SwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = module.text,
            color = module.color,
            isChecked = getCurrentValue(module),
            onValueChanged = { newValue -> setCurrentValue(module, newValue) })
    )
}