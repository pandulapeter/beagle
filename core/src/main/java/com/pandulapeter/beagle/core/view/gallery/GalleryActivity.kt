package com.pandulapeter.beagle.core.view.gallery

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.tintedDrawable
import com.pandulapeter.beagle.core.view.gallery.list.GalleryAdapter

internal class GalleryActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(GalleryViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_gallery)
        supportActionBar?.hide()
        findViewById<ImageView>(R.id.beagle_close_button).apply {
            setImageDrawable(tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary)))
            setOnClickListener { onBackPressed() }
        }
        val recyclerView = findViewById<RecyclerView>(R.id.beagle_recycler_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        recyclerView.setPadding(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, it.systemWindowInsetBottom)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                    }
                }
                requestApplyInsets()
            }
        }
        val adapter = GalleryAdapter { position -> viewModel.items.value?.get(position)?.id?.let(::onItemSelected) }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        viewModel.items.observe(this, Observer { adapter.submitList(it) })
        viewModel.loadMedia(this)
    }

    private fun onItemSelected(fileName: String) {
        val uri = getUriForFile(getScreenCapturesFolder().resolve(fileName))
        when {
            fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> shareFile(uri, ScreenCaptureManager.IMAGE_TYPE)
            fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> shareFile(uri, ScreenCaptureManager.VIDEO_TYPE)
        }
    }
}