package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.CheckBoxCellRenameMe
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.CheckBoxModuleRenameMe

internal class CheckBoxDelegateRenameMe : PersistableModuleDelegate.Boolean<CheckBoxModuleRenameMe>() {

    override fun createCells(module: CheckBoxModuleRenameMe): List<Cell<*>> = listOf(
        CheckBoxCellRenameMe(
            id = module.id,
            text = module.text,
            color = module.color,
            isChecked = getCurrentValue(module),
            onValueChanged = { setCurrentValue(module, it) })
    )
}