package com.pandulapeter.beagle.core.list.moduleDelegates

import android.content.Intent
import android.provider.Settings
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule

internal class DeveloperOptionsButtonDelegate : Module.Delegate<DeveloperOptionsButtonModule> {

    override fun createCells(module: DeveloperOptionsButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                BeagleCore.implementation.currentActivity?.run {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
                        if (module.shouldOpenInNewTask) {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    })
                }
                module.onButtonPressed()
            }
        )
    )
}