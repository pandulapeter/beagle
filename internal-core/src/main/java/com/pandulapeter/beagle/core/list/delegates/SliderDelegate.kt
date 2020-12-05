package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SliderCell
import com.pandulapeter.beagle.core.list.delegates.shared.ValueWrapperModuleDelegate
import com.pandulapeter.beagle.modules.SliderModule

internal class SliderDelegate : ValueWrapperModuleDelegate.Integer<SliderModule>() {

    override fun createCells(module: SliderModule): List<Cell<*>> = getUiValue(module).let { uiValue ->
        listOf(
            SliderCell(
                id = module.id,
                text = module.text(uiValue).let { text ->
                    if (module.shouldRequireConfirmation && hasPendingChanges(module)) text.withSuffix("*") else text
                },
                value = uiValue,
                minimumValue = module.minimumValue,
                maximumValue = module.maximumValue,
                isEnabled = module.isEnabled,
                onValueChanged = { setUiValue(module, it) }
            )
        )
    }
}