package com.pandulapeter.beagle.core.manager

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.core.util.extension.recordScreenWithMediaProjectionManager
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithDrawingCache
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithMediaProjectionManager
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.core.view.gallery.GalleryActivity

internal class ScreenCaptureManager {

    var onScreenCaptureReady: ((Uri?) -> Unit)? = null
    private val currentActivity get() = BeagleCore.implementation.currentActivity
    private val getImageFileName get() = BeagleCore.implementation.behavior.screenCaptureBehavior.getImageFileName
    private val onImageReady get() = BeagleCore.implementation.behavior.screenCaptureBehavior.onImageReady
    private val getVideoFileName get() = BeagleCore.implementation.behavior.screenCaptureBehavior.getVideoFileName
    private val onVideoReady get() = BeagleCore.implementation.behavior.screenCaptureBehavior.onVideoReady

    fun takeScreenshot() {
        if (onScreenCaptureReady != null) {
            onImageReady?.invoke(null)
        } else {
            onScreenCaptureReady = { uri ->
                onImageReady?.invoke(uri)
                onScreenCaptureReady = null
            }
            currentActivity?.run {
                val fileName = "${getImageFileName(currentTimestamp)}$IMAGE_EXTENSION"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    takeScreenshotWithMediaProjectionManager(fileName)
                } else {
                    takeScreenshotWithDrawingCache(fileName)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recordScreen() {
        if (onScreenCaptureReady != null) {
            onVideoReady?.invoke(null)
        } else {
            onScreenCaptureReady = { uri ->
                onVideoReady?.invoke(uri)
                onScreenCaptureReady = null
            }
            currentActivity?.recordScreenWithMediaProjectionManager("${getVideoFileName(currentTimestamp)}$VIDEO_EXTENSION")
        }
    }

    fun openGallery() = performOnHide { currentActivity?.run { startActivity(Intent(this, GalleryActivity::class.java)) } }

    companion object {
        const val IMAGE_EXTENSION = ".png"
        const val IMAGE_TYPE = "image/png"
        const val VIDEO_EXTENSION = ".mp4"
        const val VIDEO_TYPE = "video/mp4"
    }
}