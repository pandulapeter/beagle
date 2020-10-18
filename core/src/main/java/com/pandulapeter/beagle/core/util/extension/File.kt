package com.pandulapeter.beagle.core.util.extension

import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.view.gallery.list.ImageViewHolder
import com.pandulapeter.beagle.core.view.gallery.list.VideoViewHolder
import java.io.File

internal fun List<File>.toGalleryModels(selectedItemIds: List<String>) = mapNotNull { file ->
    file.name.let { fileName ->
        when {
            fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> ImageViewHolder.UiModel(fileName, selectedItemIds.contains(fileName), file.lastModified())
            fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> VideoViewHolder.UiModel(fileName, selectedItemIds.contains(fileName), file.lastModified())
            else -> null
        }
    }
}