package com.pandulapeter.beagle.core.list.moduleDelegates

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.PixelCopy
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.ScreenshotButtonModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal class ScreenshotButtonDelegate : Module.Delegate<ScreenshotButtonModule> {

    override fun createCells(module: ScreenshotButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            onButtonPressed = {
                module.onButtonPressed()
                val listener = object : VisibilityListener {
                    override fun onHidden() {
                        BeagleCore.implementation.removeVisibilityListener(this)
                        BeagleCore.implementation.currentActivity?.takeAndShareScreenshot()
                    }
                }
                BeagleCore.implementation.addInternalVisibilityListener(listener)
                if (!BeagleCore.implementation.hide()) {
                    listener.onHidden()
                }
            }
        )
    )

    internal fun Activity.takeAndShareScreenshot() = getScreenshot { shareImage(it) }

    private fun Activity.shareImage(image: Bitmap) {
        GlobalScope.launch(Dispatchers.IO) {
            val imagesFolder = File(cacheDir, "beagleScreenshots")
            val uri: Uri?
            try {
                imagesFolder.mkdirs()
                val file = File(imagesFolder, "screenshot_${System.currentTimeMillis()}.png")
                val stream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".beagle.fileProvider", file)
                startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_STREAM, uri)
                }, "Share"))
            } catch (_: IOException) {
            }
        }
    }

    private fun Activity.getScreenshot(callback: (Bitmap) -> Unit) {
        val rootView = findRootViewGroup()
        val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PixelCopy.request(
                window,
                Rect(0, 0, rootView.width, rootView.height),
                bitmap, {
                    if (it == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    }
                },
                Handler(Looper.getMainLooper())
            )

        } else {
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
                    AppCompatResources.getDrawable(this, typedValue.resourceId)?.draw(canvas)
                }
            }
            rootView.draw(canvas)
            callback(bitmap)
        }
    }

    private fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)
}