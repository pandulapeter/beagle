package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.ID
import com.pandulapeter.beagle.modules.HeaderModule.Companion.ID


/**
 * Displays information about the current device and the OS.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module that will be displayed in the header of the list. "Device info" by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 */
data class DeviceInfoModule(
    override val title: CharSequence = "Device info",
    override val isExpandedInitially: Boolean = false
) : ExpandableModule<DeviceInfoModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "deviceInfo"
    }
}