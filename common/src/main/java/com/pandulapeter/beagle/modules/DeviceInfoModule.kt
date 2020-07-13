package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.ID


/**
 * Displays information about the current device and the OS.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module that will be displayed in the header of the list. "Device info" by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param shouldShowManufacturer - Whether or not the device manufacturer should be part of the list. True by default.
 * @param shouldShowModel - Whether or not the device model should be part of the list. True by default.
 * @param shouldShowDisplayMetrics - Whether or not the device's screen resolution and density should be part of the list. True by default.
 * @param shouldShowAndroidVersion - Whether or not the device's Android SDK version should be part of the list. True by default.
 */
data class DeviceInfoModule(
    override val title: CharSequence = "Device info",
    override val isExpandedInitially: Boolean = false,
    val shouldShowManufacturer: Boolean = true,
    val shouldShowModel: Boolean = true,
    val shouldShowDisplayMetrics: Boolean = true,
    val shouldShowAndroidVersion: Boolean = true
) : ExpandableModule<DeviceInfoModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "deviceInfo"
    }
}