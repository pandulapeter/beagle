package com.pandulapeter.beagle.core.list.delegates

import android.os.Build
import android.util.DisplayMetrics
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemKeyValueCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.DeviceInfoModule
import kotlin.math.roundToInt

internal class DeviceInfoDelegate : ExpandableModuleDelegate<DeviceInfoModule> {

    override fun canExpand(module: DeviceInfoModule) = module.shouldShowManufacturer
            || module.shouldShowModel
            || module.shouldShowResolutionsPx
            || module.shouldShowResolutionsDp
            || module.shouldShowDensity
            || module.shouldShowAndroidVersion

    override fun MutableList<Cell<*>>.addItems(module: DeviceInfoModule) = getDeviceInfo(
        shouldShowManufacturer = module.shouldShowManufacturer,
        shouldShowModel = module.shouldShowModel,
        shouldShowResolutionsPx = module.shouldShowResolutionsPx,
        shouldShowResolutionsDp = module.shouldShowResolutionsDp,
        shouldShowDensity = module.shouldShowDensity,
        shouldShowAndroidVersion = module.shouldShowAndroidVersion
    ).forEach { (key, value) ->
        add(
            ExpandedItemKeyValueCell(
                id = "${module.id}_$key",
                key = key,
                value = value
            )
        )
    }

    //TODO: Keys should come from Appearance.
    companion object {
        fun getDeviceInfo(
            shouldShowManufacturer: Boolean,
            shouldShowModel: Boolean,
            shouldShowResolutionsPx: Boolean,
            shouldShowResolutionsDp: Boolean,
            shouldShowDensity: Boolean,
            shouldShowAndroidVersion: Boolean
        ): List<Pair<String, String>> = mutableListOf<Pair<String, String>>().apply {
            if (shouldShowManufacturer) {
                add("Manufacturer" to Build.MANUFACTURER)
            }
            if (shouldShowModel) {
                add("Model" to Build.MODEL)
            }
            val dm = DisplayMetrics()
            BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(dm)?.let {
                if (shouldShowResolutionsPx) {
                    add("Resolution (px)" to "${dm.widthPixels} * ${dm.heightPixels}")
                    if (shouldShowResolutionsDp) {
                        add("Resolution (dp)" to "${(dm.widthPixels / dm.density).roundToInt()} * ${(dm.heightPixels / dm.density).roundToInt()}")
                    }
                    if (shouldShowDensity) {
                        add("Density (dpi)" to "${dm.densityDpi}")
                    }
                }
            }
            if (shouldShowAndroidVersion) {
                add("Android SDK version" to Build.VERSION.SDK_INT.toString())
            }
        }
    }
}