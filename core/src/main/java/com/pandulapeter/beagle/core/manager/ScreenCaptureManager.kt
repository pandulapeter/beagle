package com.pandulapeter.beagle.core.manager

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.recordScreenWithMediaProjectionManager
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithDrawingCache
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithMediaProjectionManager
import com.pandulapeter.beagle.core.util.performOnHide
import com.pandulapeter.beagle.core.view.gallery.GalleryActivity

internal class ScreenCaptureManager {

    var onScreenCaptureReady: ((Uri?) -> Unit)? = null
    private val currentActivity get() = BeagleCore.implementation.currentActivity
    private val behavior get() = BeagleCore.implementation.behavior

    fun takeScreenshot(callback: (Uri?) -> Unit) {
        if (onScreenCaptureReady != null) {
            callback(null)
        } else {
            onScreenCaptureReady = { uri ->
                callback(uri)
                onScreenCaptureReady = null
            }
            currentActivity?.run {
                val fileName = "${behavior.getImageFileName()}$IMAGE_EXTENSION"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    takeScreenshotWithMediaProjectionManager(fileName)
                } else {
                    takeScreenshotWithDrawingCache(fileName)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recordScreen(callback: (Uri?) -> Unit) {
        if (onScreenCaptureReady != null) {
            callback(null)
        } else {
            onScreenCaptureReady = { uri ->
                callback(uri)
                onScreenCaptureReady = null
            }
            currentActivity?.recordScreenWithMediaProjectionManager("${behavior.getVideoFileName()}$VIDEO_EXTENSION")
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