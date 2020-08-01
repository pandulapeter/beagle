package com.pandulapeter.beagle.core.view.gallery

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.tintedDrawable
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.view.gallery.list.GalleryAdapter

internal class GalleryActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(GalleryViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_gallery)
        supportActionBar?.hide()
        findViewById<MaterialToolbar>(R.id.beagle_toolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary))
            title = BeagleCore.implementation.appearance.galleryTitle
        }
        val emptyStateTextView = findViewById<TextView>(R.id.beagle_text_view)
        emptyStateTextView.text = BeagleCore.implementation.appearance.galleryNoMediaMessage
        val largePadding = dimension(R.dimen.beagle_large_content_padding)
        val recyclerView = findViewById<RecyclerView>(R.id.beagle_recycler_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        recyclerView.setPadding(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, it.systemWindowInsetBottom)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                        emptyStateTextView.setPadding(largePadding, largePadding, largePadding, largePadding + it.systemWindowInsetBottom)
                    }
                }
                requestApplyInsets()
            }
        }
        val adapter = GalleryAdapter { position -> viewModel.items.value?.get(position)?.id?.let(::onItemSelected) }
        recyclerView.setHasFixedSize(true)
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        recyclerView.layoutManager = GridLayoutManager(this, displayMetrics.widthPixels / dimension(R.dimen.beagle_gallery_item_minimum_size))
        recyclerView.adapter = adapter
        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
            emptyStateTextView.visible = it.isEmpty()
        })
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