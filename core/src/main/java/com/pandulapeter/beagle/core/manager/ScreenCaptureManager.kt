package com.pandulapeter.beagle.core.manager

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.createScreenshotFromBitmap
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithDrawingCache
import com.pandulapeter.beagle.core.util.extension.takeScreenshotWithMediaProjectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class ScreenCaptureManager {

    var onScreenshotReady: ((Bitmap?) -> Unit)? = null
    private val currentActivity get() = BeagleCore.implementation.currentActivity

    fun takeScreenshot(fileName: String, callback: (Uri?) -> Unit) {
        if (onScreenshotReady != null) {
            callback(null)
        } else {
            onScreenshotReady = { bitmap ->
                if (bitmap == null) {
                    callback(null)
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        (currentActivity?.createScreenshotFromBitmap(bitmap, fileName))?.let { uri ->
                            launch(Dispatchers.Main) { callback(uri) }
                        }
                    }
                }
                onScreenshotReady = null
            }
            currentActivity?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    takeScreenshotWithMediaProjectionManager()
                } else {
                    takeScreenshotWithDrawingCache()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recordScreen(fileName: String, callback: (Uri?) -> Unit) {
        //TODO
    }
}