package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SliderCell
import com.pandulapeter.beagle.core.list.delegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.modules.SliderModule

internal class SliderDelegate : PersistableModuleDelegate.Integer<SliderModule>() {

    override fun createCells(module: SliderModule): List<Cell<*>> = listOf(
        SliderCell(
            id = module.id,
            text = if (module.shouldRequireConfirmation && hasPendingChanges(module)) module.text(getUiValue(module)).append("*") else module.text(getUiValue(module)),
            value = getUiValue(module),
            minimumValue = module.minimumValue,
            maximumValue = module.maximumValue,
            onValueChanged = { setUiValue(module, it) })
    )
}