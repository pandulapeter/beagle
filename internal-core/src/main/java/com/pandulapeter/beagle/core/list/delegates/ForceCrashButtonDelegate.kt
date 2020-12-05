package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.util.createTextModuleFromType
import com.pandulapeter.beagle.modules.ForceCrashButtonModule

internal class ForceCrashButtonDelegate : Module.Delegate<ForceCrashButtonModule> {

    override fun createCells(module: ForceCrashButtonModule): List<Cell<*>> = listOf(
        createTextModuleFromType(
            type = module.type,
            id = module.id,
            text = module.text,
            isEnabled = module.isEnabled,
            icon = module.icon,
            onItemSelected = {
                module.onButtonPressed()
                throw RuntimeException(module.exception)
            }
        )
    )
}