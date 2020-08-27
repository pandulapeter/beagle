package com.pandulapeter.beagle.core.list.delegates

import android.os.Build
import android.util.DisplayMetrics
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.KeyValueCell
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

    override fun MutableList<Cell<*>>.addItems(module: DeviceInfoModule) {
        if (module.shouldShowManufacturer) {
            add(
                KeyValueCell(
                    id = "${module.id}_manufacturer",
                    key = "Manufacturer",
                    value = Build.MANUFACTURER
                )
            )
        }
        if (module.shouldShowModel) {
            add(
                KeyValueCell(
                    id = "${module.id}_model",
                    key = "Model",
                    value = Build.MODEL
                )
            )
        }
        val dm = DisplayMetrics()
        BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(dm)?.let {
            if (module.shouldShowResolutionsPx) {
                add(
                    KeyValueCell(
                        id = "${module.id}_resolution_px",
                        key = "Resolution (px)",
                        value = "${dm.widthPixels} * ${dm.heightPixels}"
                    )
                )
                if (module.shouldShowResolutionsDp) {
                    add(
                        KeyValueCell(
                            id = "${module.id}_resolution_dp",
                            key = "Resolution (dp)",
                            value = "${(dm.widthPixels / dm.density).roundToInt()} * ${(dm.heightPixels / dm.density).roundToInt()}"
                        )
                    )
                }
                if (module.shouldShowDensity) {
                    add(
                        KeyValueCell(
                            id = "${module.id}_density",
                            key = "Density (dpi)",
                            value = "${dm.densityDpi}"
                        )
                    )
                }
            }
        }
        if (module.shouldShowAndroidVersion) {
            add(
                KeyValueCell(
                    id = "${module.id}_sdkVersion",
                    key = "Android SDK version",
                    value = Build.VERSION.SDK_INT.toString()
                )
            )
        }
    }
}