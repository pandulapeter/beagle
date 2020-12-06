package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.util.createTextModuleFromType
import com.pandulapeter.beagle.modules.ScreenshotButtonModule

internal class ScreenshotButtonDelegate : Module.Delegate<ScreenshotButtonModule> {

    override fun createCells(module: ScreenshotButtonModule): List<Cell<*>> = listOf(
        createTextModuleFromType(
            type = module.type,
            id = module.id,
            text = module.text,
            isEnabled = module.isEnabled,
            icon = module.icon,
            onItemSelected = {
                module.onButtonPressed()
                hideDebugMenuAndTakeScreenshot()
            }
        )
    )

    companion object {
        fun hideDebugMenuAndTakeScreenshot() = BeagleCore.implementation.performOnHide(BeagleCore.implementation::takeScreenshot)
    }
}