package com.pandulapeter.beagle.core.list.moduleDelegates

import android.os.Build
import android.util.DisplayMetrics
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.DeviceInfoModule

internal class DeviceInfoDelegate : ExpandableModuleDelegate<DeviceInfoModule> {

    //TODO: Create a special cell / module type for key-value pairs.
    override fun MutableList<Cell<*>>.addItems(module: DeviceInfoModule) {
        add(
            TextCell(
                id = "${module.id}_manufacturer",
                text = "• Manufacturer: ${Build.MANUFACTURER}",
                onItemSelected = null
            )
        )
        add(
            TextCell(
                id = "${module.id}_model",
                text = "• Model: ${Build.MODEL}",
                onItemSelected = null
            )
        )
        val dm = DisplayMetrics()
        BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(dm)?.let {
            add(
                TextCell(
                    id = "${module.id}_screen",
                    text = "• Screen: ${dm.widthPixels} * ${dm.heightPixels} (${dm.densityDpi} dpi)",
                    onItemSelected = null
                )
            )
        }
        add(
            TextCell(
                id = "${module.id}_sdkVersion",
                text = "• Android SDK version: ${Build.VERSION.SDK_INT}",
                onItemSelected = null
            )
        )
    }
}