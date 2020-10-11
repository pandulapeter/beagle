package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_SHOULD_SHOW_ANDROID_VERSION
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_SHOULD_SHOW_DENSITY
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_SHOULD_SHOW_MANUFACTURER
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_SHOULD_SHOW_MODEL
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_SHOULD_SHOW_RESOLUTION_DP
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_SHOULD_SHOW_RESOLUTION_PX
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.DEFAULT_TITLE
import com.pandulapeter.beagle.modules.DeviceInfoModule.Companion.ID


/**
 * Displays information about the current device and the OS.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title of the module that will be displayed in the header of the list. [DEFAULT_TITLE] by default.
 * @param shouldShowManufacturer - Whether or not the device manufacturer should be part of the list. [DEFAULT_SHOULD_SHOW_MANUFACTURER] by default.
 * @param shouldShowModel - Whether or not the device model should be part of the list. [DEFAULT_SHOULD_SHOW_MODEL] by default.
 * @param shouldShowResolutionsPx - Whether or not the device's screen resolution (in pixels) should be part of the list. [DEFAULT_SHOULD_SHOW_RESOLUTION_PX] by default.
 * @param shouldShowResolutionsDp - Whether or not the device's screen resolution (in dp) should be part of the list. [DEFAULT_SHOULD_SHOW_RESOLUTION_DP] by default.
 * @param shouldShowDensity - Whether or not the device's screen density should be part of the list. [DEFAULT_SHOULD_SHOW_DENSITY] by default.
 * @param shouldShowAndroidVersion - Whether or not the device's Android SDK version should be part of the list. [DEFAULT_SHOULD_SHOW_ANDROID_VERSION] by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 */
data class DeviceInfoModule(
    val title: Text = Text.CharSequence(DEFAULT_TITLE),
    val shouldShowManufacturer: Boolean = DEFAULT_SHOULD_SHOW_MANUFACTURER,
    val shouldShowModel: Boolean = DEFAULT_SHOULD_SHOW_MODEL,
    val shouldShowResolutionsPx: Boolean = DEFAULT_SHOULD_SHOW_RESOLUTION_PX,
    val shouldShowResolutionsDp: Boolean = DEFAULT_SHOULD_SHOW_RESOLUTION_DP,
    val shouldShowDensity: Boolean = DEFAULT_SHOULD_SHOW_DENSITY,
    val shouldShowAndroidVersion: Boolean = DEFAULT_SHOULD_SHOW_ANDROID_VERSION,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY
) : ExpandableModule<DeviceInfoModule> {

    override val id: String = ID

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        const val ID = "deviceInfo"
        private const val DEFAULT_TITLE = "Device info"
        private const val DEFAULT_SHOULD_SHOW_MANUFACTURER = true
        private const val DEFAULT_SHOULD_SHOW_MODEL = true
        private const val DEFAULT_SHOULD_SHOW_RESOLUTION_PX = true
        private const val DEFAULT_SHOULD_SHOW_RESOLUTION_DP = true
        private const val DEFAULT_SHOULD_SHOW_DENSITY = true
        private const val DEFAULT_SHOULD_SHOW_ANDROID_VERSION = true
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}