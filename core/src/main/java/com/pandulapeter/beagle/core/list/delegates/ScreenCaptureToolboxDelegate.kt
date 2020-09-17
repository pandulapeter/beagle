package com.pandulapeter.beagle.core.list.delegates

import android.os.Build
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule

internal class ScreenCaptureToolboxDelegate : ExpandableModuleDelegate<ScreenCaptureToolboxModule> {

    override fun canExpand(module: ScreenCaptureToolboxModule) = true

    override fun MutableList<Cell<*>>.addItems(module: ScreenCaptureToolboxModule) {
        module.imageText?.let { imageText ->
            add(
                ExpandedItemTextCell(
                    id = "${module.id}_image",
                    text = imageText,
                    isEnabled = true,
                    onItemSelected = ScreenshotButtonDelegate.Companion::hideDebugMenuAndTakeScreenshot
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            module.videoText?.let { videoText ->
                add(
                    ExpandedItemTextCell(
                        id = "${module.id}_video",
                        text = videoText,
                        isEnabled = true,
                        onItemSelected = ScreenRecordingButtonDelegate.Companion::hideDebugMenuAndRecordScreen
                    )
                )
            }
        }
        add(
            ExpandedItemTextCell(
                id = "${module.id}_gallery",
                text = module.galleryText,
                isEnabled = true,
                onItemSelected = BeagleCore.implementation::openGallery
            )
        )
    }
}