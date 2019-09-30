package com.pandulapeter.beagle.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.Dimension
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagle.utils.drawable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.CoroutineContext

internal class DebugMenuDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    oldViews: List<View> = emptyList(),
    drawer: View? = null,
    @Dimension drawerWidth: Int? = null
) : DrawerLayout(context, attrs, defStyleAttr) {

    private val container = KeylineOverlayFrameLayout(context)
    var keylineOverlay
        get() = container.keylineOverlayToggle
        set(value) {
            container.keylineOverlayToggle = value
        }
    private var currentJob: CoroutineContext? = null

    fun takeAndShareScreenshot() = shareImage(getScreenshot())

    private fun shareImage(image: Bitmap) {
        currentJob?.cancel()
        currentJob = GlobalScope.launch {
            val imagesFolder = File(context.cacheDir, "images")
            val uri: Uri?
            try {
                imagesFolder.mkdirs()
                val file = File(imagesFolder, "screenshot_${System.currentTimeMillis()}.png")
                val stream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".debugMenu.fileProvider", file)
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_STREAM, uri)
                }, "Share"))
            } catch (_: IOException) {
            }
            currentJob = null
        }
    }

    //TODO: System bars should be included in the screenshot
    private fun getScreenshot(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = container.rootView.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                canvas.drawColor(typedValue.data)
            } else {
                context.drawable(typedValue.resourceId)?.draw(canvas)
            }
        }
        container.draw(canvas)
        return bitmap
    }

    init {
        addView(container.apply { oldViews.forEach { view -> addView(view) } }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(drawer, LayoutParams(drawerWidth ?: context.dimension(R.dimen.drawer_width), LayoutParams.MATCH_PARENT, GravityCompat.END))
    }
}
