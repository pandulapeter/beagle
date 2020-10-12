package com.pandulapeter.beagle.core.util.extension

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.OverlayFragment
import com.pandulapeter.beagle.utils.extensions.drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private val excludedPackageNames = listOf(
    "com.pandulapeter.beagle.core.view.gallery.GalleryActivity",
    "com.pandulapeter.beagle.core.view.bugReport.BugReportActivity",
    "com.pandulapeter.beagle.implementation.DebugMenuActivity",
    "com.markodevcic.peko.PekoActivity"
)

internal val Activity.supportsDebugMenu
    get() = this is FragmentActivity
            && excludedPackageNames.none { componentName.className.startsWith(it) }
            && BeagleCore.implementation.behavior.excludedPackageNames.none { componentName.className.startsWith(it) }


internal fun Activity.shareFile(uri: Uri, fileType: String) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = fileType
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        putExtra(Intent.EXTRA_STREAM, uri)
    }, null))
}

internal fun Activity.shareText(text: String) {
    try {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }, null))
    } catch (_: Exception) {
        Toast.makeText(this, text(BeagleCore.implementation.appearance.generalTexts.shareErrorText), Toast.LENGTH_LONG).show()
    }
}

internal fun Activity.shareFiles(uris: List<Uri>) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = "*/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
    }, null))
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal fun Activity.takeScreenshotWithMediaProjectionManager(fileName: String) {
    (BeagleCore.implementation.uiManager.findOverlayFragment(this as? FragmentActivity?) as? OverlayFragment?).let { overlayFragment ->
        overlayFragment?.startCapture(false, fileName) ?: BeagleCore.implementation.onScreenCaptureReady?.invoke(null)
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal fun Activity.recordScreenWithMediaProjectionManager(fileName: String) {
    (BeagleCore.implementation.uiManager.findOverlayFragment(this as? FragmentActivity?) as? OverlayFragment?).let { overlayFragment ->
        overlayFragment?.startCapture(true, fileName) ?: BeagleCore.implementation.onScreenCaptureReady?.invoke(null)
    }
}

internal fun Activity.takeScreenshotWithDrawingCache(fileName: String) {
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
    GlobalScope.launch(Dispatchers.IO) {
        (createScreenshotFromBitmap(bitmap, fileName))?.let { uri ->
            launch(Dispatchers.Main) {
                BeagleCore.implementation.onScreenCaptureReady?.invoke(uri)
            }
        }
    }
}

private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)