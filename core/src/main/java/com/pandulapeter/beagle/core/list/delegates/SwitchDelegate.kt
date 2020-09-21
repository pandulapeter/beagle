package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.delegates.shared.ValueWrapperModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.SwitchModule

internal class SwitchDelegate : ValueWrapperModuleDelegate.Boolean<SwitchModule>() {

    override fun createCells(module: SwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = if (module.shouldRequireConfirmation && hasPendingChanges(module)) module.text.append("*") else module.text,
            isChecked = getUiValue(module),
            isEnabled = module.isEnabled,
            onValueChanged = { newValue -> setUiValue(module, newValue) }
        )
    )
}