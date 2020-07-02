package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.CheckBoxCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.CheckBoxModule

internal class CheckBoxDelegate : PersistableModuleDelegate.Boolean<CheckBoxModule>() {

    override fun createCells(module: CheckBoxModule): List<Cell<*>> = listOf(
        CheckBoxCell(
            id = module.id,
            text = if (module.shouldRequireConfirmation && hasPendingChanges(module)) module.text.append("*") else module.text,
            isChecked = getUiValue(module),
            onValueChanged = { setUiValue(module, it) })
    )
}