package com.pandulapeter.beagle.core.view.gallery

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.shareFiles
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.viewModel
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.view.gallery.list.GalleryAdapter
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class GalleryActivity : AppCompatActivity(), DeleteConfirmationDialogFragment.OnPositiveButtonClickedListener {

    private val viewModel by viewModel<GalleryViewModel>()
    private val contentPadding by lazy { dimension(R.dimen.beagle_content_padding) }
    private lateinit var shareButton: MenuItem
    private lateinit var deleteButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_gallery)
        supportActionBar?.hide()
        val toolbar = findViewById<Toolbar>(R.id.beagle_toolbar).apply {
            val textColor = colorResource(android.R.attr.textColorPrimary)
            setNavigationOnClickListener { supportFinishAfterTransition() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, textColor)
            title = text(BeagleCore.implementation.appearance.galleryTexts.title)
            shareButton = menu.findItem(R.id.beagle_share).also {
                it.title = text(BeagleCore.implementation.appearance.generalTexts.shareHint)
                it.icon = tintedDrawable(R.drawable.beagle_ic_share, textColor)
            }
            deleteButton = menu.findItem(R.id.beagle_delete).also {
                it.title = text(BeagleCore.implementation.appearance.galleryTexts.deleteHint)
                it.icon = tintedDrawable(R.drawable.beagle_ic_delete, textColor)
            }
            setOnMenuItemClickListener(::onMenuItemClicked)
        }
        viewModel.isInSelectionMode.observe(this) {
            shareButton.isVisible = it
            deleteButton.isVisible = it
        }
        val emptyStateTextView = findViewById<TextView>(R.id.beagle_text_view)
        emptyStateTextView.text = text(BeagleCore.implementation.appearance.galleryTexts.noMediaMessage)
        val largePadding = dimension(R.dimen.beagle_large_content_padding)
        val recyclerView = findViewById<RecyclerView>(R.id.beagle_recycler_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        toolbar.setPadding(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, 0)
                        recyclerView.setPadding(contentPadding, 0, contentPadding, it.systemWindowInsetBottom + contentPadding)
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
        val spanCount = getSpanCount()
        recyclerView.layoutManager = GridLayoutManager(this, spanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (viewModel.isSectionHeader(position)) spanCount else 1
            }
        }
        recyclerView.adapter = adapter
        viewModel.items.observe(this, {
            adapter.submitList(it)
            emptyStateTextView.visible = it.isEmpty()
        })
        findViewById<ProgressBar>(R.id.beagle_progress_bar).let { progressBar ->
            viewModel.shouldShowLoadingIndicator.observe(this) { progressBar.visible = it }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMedia()
    }

    override fun onPositiveButtonClicked() = viewModel.deleteSelectedItems()

    fun loadMedia() = viewModel.loadMedia(this)

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume { shareItems(viewModel.selectedItemIds) }
        R.id.beagle_delete -> consume { DeleteConfirmationDialogFragment.show(supportFragmentManager, viewModel.selectedItemIds.size > 1) }
        else -> false
    }

    private fun onItemSelected(fileName: String) {
        if (viewModel.isInSelectionMode.value == true) {
            viewModel.selectItem(fileName)
        } else {
            MediaPreviewDialogFragment.show(supportFragmentManager, fileName)
        }
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