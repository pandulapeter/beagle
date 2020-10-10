package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.CheckBoxCell
import com.pandulapeter.beagle.core.list.delegates.shared.ValueWrapperModuleDelegate
import com.pandulapeter.beagle.modules.CheckBoxModule

internal class CheckBoxDelegate : ValueWrapperModuleDelegate.Boolean<CheckBoxModule>() {

    override fun createCells(module: CheckBoxModule): List<Cell<*>> = getUiValue(module).let { uiValue ->
        listOf(
            CheckBoxCell(
                id = module.id,
                text = module.text(uiValue).let { title ->
                    if (module.shouldRequireConfirmation && hasPendingChanges(module)) title.withSuffix("*") else title
                },
                isChecked = uiValue,
                isEnabled = module.isEnabled,
                onValueChanged = { setUiValue(module, it) })
        )
    }
}