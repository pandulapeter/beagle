package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.GalleryButtonModule

internal class GalleryButtonDelegate : Module.Delegate<GalleryButtonModule> {

    override fun createCells(module: GalleryButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                BeagleCore.implementation.openGallery()
            }
        )
    )
}