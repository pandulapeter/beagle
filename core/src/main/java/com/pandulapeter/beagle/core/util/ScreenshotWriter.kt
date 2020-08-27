package com.pandulapeter.beagle.core.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Build
import android.os.Handler
import android.view.Surface
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class ScreenshotWriter(
    private val width: Int,
    private val height: Int,
    handler: Handler,
    private val callback: (Bitmap) -> Unit
) : OnImageAvailableListener {

    val surface: Surface get() = imageReader.surface

    @SuppressLint("WrongConstant")
    //TODO: private val imageReader = ImageReader.newInstance(width, height, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ImageFormat.FLEX_RGBA_8888 else PixelFormat.RGBA_8888, 1)
    private val imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1)
    private var latestBitmap: Bitmap? = null

    init {
        imageReader.setOnImageAvailableListener(this, handler)
    }

    fun forceTry() = imageReader.acquireLatestImage().let {
        if (it == null) {
            false
        } else {
            handleImage(it)
            true
        }
    }

    override fun onImageAvailable(reader: ImageReader) {
        val image = imageReader.acquireLatestImage()
        if (image != null) {
            handleImage(image)
        }
    }

    private fun handleImage(image: Image) {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        val bitmapWidth = width + rowPadding / pixelStride
        if (latestBitmap?.width != bitmapWidth || latestBitmap?.height != height) {
            latestBitmap?.recycle()
            latestBitmap = Bitmap.createBitmap(bitmapWidth, height, Bitmap.Config.ARGB_8888)
        }
        latestBitmap?.copyPixelsFromBuffer(buffer)
        image.close()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val croppedBitmap = Bitmap.createBitmap(latestBitmap!!, 0, 0, width, height)
        croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        callback(croppedBitmap)
        imageReader.close()
    }
}