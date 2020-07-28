package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.modules.ScreenshotButtonModule

internal class ScreenshotButtonDelegate : Module.Delegate<ScreenshotButtonModule> {

    override fun createCells(module: ScreenshotButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                hideDebugMenuAndTakeScreenshot()
            }
        )
    )

    companion object {
        fun hideDebugMenuAndTakeScreenshot() = performOnHide {
            BeagleCore.implementation.takeScreenshot { uri ->
                if (uri != null) {
                    BeagleCore.implementation.currentActivity?.shareFile(uri, ScreenCaptureManager.IMAGE_TYPE)
                }
            }
        }
    }
}