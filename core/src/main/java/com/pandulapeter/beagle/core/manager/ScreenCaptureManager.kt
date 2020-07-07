package com.pandulapeter.beagle.core.manager

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.recordScreenWithMediaProjectionManager
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithDrawingCache
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithMediaProjectionManager

internal class ScreenCaptureManager {

    var onScreenCaptureReady: ((Uri?) -> Unit)? = null
    private val currentActivity get() = BeagleCore.implementation.currentActivity

    fun takeScreenshot(fileName: String, callback: (Uri?) -> Unit) {
        if (onScreenCaptureReady != null) {
            callback(null)
        } else {
            onScreenCaptureReady = { uri ->
                callback(uri)
                onScreenCaptureReady = null
            }
            currentActivity?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    takeScreenshotWithMediaProjectionManager(fileName)
                } else {
                    takeScreenshotWithDrawingCache(fileName)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recordScreen(fileName: String, callback: (Uri?) -> Unit) {
        if (onScreenCaptureReady != null) {
            callback(null)
        } else {
            onScreenCaptureReady = { uri ->
                callback(uri)
                onScreenCaptureReady = null
            }
            currentActivity?.recordScreenWithMediaProjectionManager(fileName)
        }
    }
}