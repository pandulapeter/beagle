package com.pandulapeter.beagle.core.list.moduleDelegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.ForceCrashButtonModule

internal class ForceCrashButtonDelegate : Module.Delegate<ForceCrashButtonModule> {

    override fun createCells(module: ForceCrashButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                throw RuntimeException(module.message)
            }
        )
    )
}