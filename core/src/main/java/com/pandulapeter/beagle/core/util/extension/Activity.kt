package com.pandulapeter.beagle.core.util.extension

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.OverlayFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private val excludedPackageNames = listOf(
    "com.pandulapeter.beagle.implementation.DebugMenuActivity"
)

internal val Activity.supportsDebugMenu
    get() = this is FragmentActivity
            && excludedPackageNames.none { componentName.className.startsWith(it) }
            && BeagleCore.implementation.behavior.excludedPackageNames.none { componentName.className.startsWith(it) }

internal suspend fun Activity.createScreenshotFromBitmap(bitmap: Bitmap, fileName: String): Uri? = withContext(Dispatchers.IO) {
    val imagesFolder = File(cacheDir, "beagleScreenshots")
    try {
        imagesFolder.mkdirs()
        val file = File(imagesFolder, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".beagle.fileProvider", file)
    } catch (_: IOException) {
        null
    }
}

internal fun Activity.shareFile(uri: Uri, fileType: String, title: String) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = fileType
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        putExtra(Intent.EXTRA_STREAM, uri)
    }, title))
}


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal fun Activity.takeScreenshotWithMediaProjectionManager() {
    (BeagleCore.implementation.uiManager.findOverlayFragment(this as? FragmentActivity?) as? OverlayFragment?).let { overlayFragment ->
        overlayFragment?.takeScreenshot() ?: BeagleCore.implementation.onScreenshotReady?.invoke(null)
    }
}

internal fun Activity.takeScreenshotWithDrawingCache() {
    val rootView = findRootViewGroup()
    val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val bgDrawable = rootView.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            canvas.drawColor(typedValue.data)
        } else {
            drawable(typedValue.resourceId)?.draw(canvas)
        }
    }
    rootView.draw(canvas)
    BeagleCore.implementation.onScreenshotReady?.invoke(bitmap)
}

private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)