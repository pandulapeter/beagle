package com.pandulapeter.beagle.core.view.gallery

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.shareFiles
import com.pandulapeter.beagle.core.util.extension.tintedDrawable
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.view.gallery.list.GalleryAdapter
import com.pandulapeter.beagle.core.view.gallery.preview.MediaPreviewDialogFragment
import com.pandulapeter.beagle.utils.consume

internal class GalleryActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(GalleryViewModel::class.java) }
    private val contentPadding by lazy { dimension(R.dimen.beagle_content_padding) }
    private lateinit var shareButton: MenuItem
    private lateinit var deleteButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_gallery)
        supportActionBar?.hide()
        findViewById<Toolbar>(R.id.beagle_toolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary))
            title = BeagleCore.implementation.appearance.galleryTitle
            val textColor = AppCompatTextView(context).textColors.defaultColor //TODO: How not to get the current theme's text color.
            shareButton = menu.findItem(R.id.beagle_share).also {
                //TODO: Menu item hint should be configurable.
                it.icon = tintedDrawable(R.drawable.beagle_ic_share, textColor)
            }
            deleteButton = menu.findItem(R.id.beagle_delete).also {
                //TODO: Menu item hint should be configurable.
                it.icon = tintedDrawable(R.drawable.beagle_ic_delete, textColor)
            }
            setOnMenuItemClickListener(::onMenuItemClicked)
        }
        viewModel.isInSelectionMode.observe(this) {
            shareButton.isVisible = it
            deleteButton.isVisible = it
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
                        recyclerView.setPadding(it.systemWindowInsetLeft + contentPadding, contentPadding, it.systemWindowInsetRight + contentPadding, it.systemWindowInsetBottom + contentPadding)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                        emptyStateTextView.setPadding(largePadding, largePadding, largePadding, largePadding + it.systemWindowInsetBottom)
                    }
                }
                requestApplyInsets()
            }
        }
        val adapter = GalleryAdapter(
            onMediaSelected = { position -> viewModel.items.value?.get(position)?.id?.let(::onItemSelected) },
            onLongTap = { position -> viewModel.items.value?.get(position)?.id?.let(viewModel::selectItem) }
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, getSpanCount())
        recyclerView.adapter = adapter
        viewModel.items.observe(this, {
            adapter.submitList(it)
            emptyStateTextView.visible = it.isEmpty()
        })
        viewModel.loadMedia(this)
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume { shareItems(viewModel.selectedItemIds) }
        R.id.beagle_delete -> consume { viewModel.deleteSelectedItems() }
        else -> false
    }

    private fun onItemSelected(fileName: String) {
        MediaPreviewDialogFragment.show(supportFragmentManager, fileName)
    }

    private fun shareItem(fileName: String) {
        val uri = getUriForFile(getScreenCapturesFolder().resolve(fileName))
        when {
            fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> shareFile(uri, ScreenCaptureManager.IMAGE_TYPE)
            fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> shareFile(uri, ScreenCaptureManager.VIDEO_TYPE)
        }
    }

    private fun shareItems(fileNames: List<String>) {
        if (fileNames.size == 1) {
            shareItem(fileNames.first())
        } else {
            shareFiles(fileNames.map { fileName -> getUriForFile(getScreenCapturesFolder().resolve(fileName)) })
        }
    }

    override fun onBackPressed() {
        if (viewModel.isInSelectionMode.value == true) {
            viewModel.exitSelectionMode()
        } else {
            super.onBackPressed()
        }
    }

    private fun getSpanCount(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / dimension(R.dimen.beagle_gallery_item_minimum_size)
    }
}