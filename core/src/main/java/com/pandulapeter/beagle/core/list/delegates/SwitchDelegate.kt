package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.delegates.shared.ValueWrapperModuleDelegate
import com.pandulapeter.beagle.modules.SwitchModule

internal class SwitchDelegate : ValueWrapperModuleDelegate.Boolean<SwitchModule>() {

    override fun createCells(module: SwitchModule): List<Cell<*>> = getUiValue(module).let { uiValue ->
        listOf(
            SwitchCell(
                id = module.id,
                text = module.text(uiValue).let { text ->
                    if (module.shouldRequireConfirmation && hasPendingChanges(module)) text.withSuffix("*") else text
                },
                isChecked = uiValue,
                isEnabled = module.isEnabled,
                onValueChanged = { newValue -> setUiValue(module, newValue) }
            )
        )
    }
}