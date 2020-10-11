package com.pandulapeter.beagle.core.list.delegates

import android.content.Intent
import android.provider.Settings
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.util.createTextModuleFromType
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule

internal class DeveloperOptionsButtonDelegate : Module.Delegate<DeveloperOptionsButtonModule> {

    override fun createCells(module: DeveloperOptionsButtonModule): List<Cell<*>> = listOf(
        createTextModuleFromType(
            type = module.type,
            id = module.id,
            text = module.text,
            isEnabled = module.isEnabled,
            icon = module.icon,
            onItemSelected = {
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