package com.pandulapeter.beagle.core.list.delegates

import android.os.Build
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule

internal class ScreenRecordingButtonDelegate : Module.Delegate<ScreenRecordingButtonModule> {

    override fun createCells(module: ScreenRecordingButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                val listener = object : VisibilityListener {
                    override fun onHidden() {
                        BeagleCore.implementation.removeVisibilityListener(this)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            BeagleCore.implementation.recordScreen { uri ->
                                if (uri != null) {
                                    BeagleCore.implementation.currentActivity?.shareFile(uri, "video/mp4")
                                }
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