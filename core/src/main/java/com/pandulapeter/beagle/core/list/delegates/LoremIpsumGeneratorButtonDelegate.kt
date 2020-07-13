package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.LoremIpsumGeneratorButtonModule

internal class LoremIpsumGeneratorButtonDelegate : Module.Delegate<LoremIpsumGeneratorButtonModule> {

    override fun createCells(module: LoremIpsumGeneratorButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onLoremIpsumReady("TODO") //TODO
            }
        )
    )
}