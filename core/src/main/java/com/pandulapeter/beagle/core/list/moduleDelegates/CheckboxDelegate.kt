package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.CheckboxCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.CheckboxModule

internal class CheckboxDelegate : PersistableModuleDelegate.Boolean<CheckboxModule>() {

    override fun createCells(module: CheckboxModule): List<Cell<*>> = listOf(
        CheckboxCell(
            id = module.id,
            text = module.text,
            color = module.color,
            isChecked = getCurrentValue(module),
            onValueChanged = { setCurrentValue(module, it) })
    )
}