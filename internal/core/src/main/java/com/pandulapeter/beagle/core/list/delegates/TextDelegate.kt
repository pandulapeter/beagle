package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.util.createTextModuleFromType
import com.pandulapeter.beagle.modules.TextModule

internal class TextDelegate : Module.Delegate<TextModule> {

    override fun createCells(module: TextModule): List<Cell<*>> = listOf(
        createTextModuleFromType(
            type = module.type,
            id = module.id,
            text = module.text,
            isEnabled = module.isEnabled,
            icon = module.icon,
            onItemSelected = module.onItemSelected
        )
    )
}