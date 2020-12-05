package com.pandulapeter.beagle.core.list.delegates

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.util.createTextModuleFromType
import com.pandulapeter.beagle.modules.AppInfoButtonModule

internal class AppInfoButtonDelegate : Module.Delegate<AppInfoButtonModule> {

    override fun createCells(module: AppInfoButtonModule): List<Cell<*>> = listOf(
        createTextModuleFromType(
            type = module.type,
            id = module.id,
            text = module.text,
            isEnabled = module.isEnabled,
            icon = module.icon,
            onItemSelected = {
                BeagleCore.implementation.currentActivity?.run {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", packageName, null)
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