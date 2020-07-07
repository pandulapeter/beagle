package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.modules.ScreenshotButtonModule

internal class ScreenshotButtonDelegate : Module.Delegate<ScreenshotButtonModule> {

    override fun createCells(module: ScreenshotButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                val listener = object : VisibilityListener {
                    override fun onHidden() {
                        BeagleCore.implementation.removeVisibilityListener(this)
                        BeagleCore.implementation.takeScreenshot(module.fileName) { uri ->
                            if (uri != null) {
                                BeagleCore.implementation.currentActivity?.shareFile(uri, "image/png")
                            }
                        }
                    }
                }
                BeagleCore.implementation.addInternalVisibilityListener(listener)
                if (!BeagleCore.implementation.hide()) {
                    listener.onHidden()
                }
            }
        )
    )
}