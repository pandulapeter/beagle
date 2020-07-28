package com.pandulapeter.beagle.core.list.delegates

import android.os.Build
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule

internal class ScreenRecordingButtonDelegate : Module.Delegate<ScreenRecordingButtonModule> {

    override fun createCells(module: ScreenRecordingButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                hideDebugMenuAndRecordScreen()
            }
        )
    )

    companion object {
        fun hideDebugMenuAndRecordScreen() = performOnHide {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BeagleCore.implementation.recordScreen { uri ->
                    if (uri != null) {
                        BeagleCore.implementation.currentActivity?.shareFile(uri, ScreenCaptureManager.VIDEO_TYPE)
                    }
                }
            }
        }
    }
}