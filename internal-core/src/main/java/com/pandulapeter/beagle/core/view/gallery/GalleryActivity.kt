package com.pandulapeter.beagle.core.view.gallery

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.getBeagleInsets
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleActivityGalleryBinding
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.*
import com.pandulapeter.beagle.core.view.gallery.list.GalleryAdapter
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class GalleryActivity : AppCompatActivity(), DeleteConfirmationDialogFragment.OnPositiveButtonClickedListener {

    private lateinit var binding: BeagleActivityGalleryBinding
    private val viewModel by viewModel<GalleryViewModel>()
    private val contentPadding by lazy { dimension(R.dimen.beagle_content_padding) }
    private lateinit var shareButton: MenuItem
    private lateinit var deleteButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        binding = BeagleActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.beagleToolbar.run {
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
        binding.beagleTextView.text = text(BeagleCore.implementation.appearance.galleryTexts.noMediaMessage)
        val largePadding = dimension(R.dimen.beagle_large_content_padding)
        binding.beagleBottomNavigationOverlay.setBackgroundColor(window.navigationBarColor)
        window.decorView.run {
            setOnApplyWindowInsetsListener { view, insets ->
                onApplyWindowInsets(insets).also {
                    val beagleInsets = WindowInsetsCompat.toWindowInsetsCompat(it, view).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                    binding.beagleToolbar.setPadding(beagleInsets.left, 0, beagleInsets.right, 0)
                    binding.beagleRecyclerView.setPadding(contentPadding, 0, contentPadding, beagleInsets.bottom + contentPadding)
                    binding.beagleBottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = beagleInsets.bottom } }
                    binding.beagleTextView.setPadding(largePadding, largePadding, largePadding, largePadding + beagleInsets.bottom)
                }
            }
            requestApplyInsets()
        }
        val galleryAdapter = GalleryAdapter(
            onMediaSelected = { position -> viewModel.items.value?.get(position)?.id?.let(::onItemSelected) },
            onLongTap = { position -> viewModel.items.value?.get(position)?.id?.let(viewModel::selectItem) }
        )
        binding.beagleRecyclerView.run {
            setHasFixedSize(true)
            val spanCount = getSpanCount()
            layoutManager = GridLayoutManager(this@GalleryActivity, spanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = if (viewModel.isSectionHeader(position)) spanCount else 1
                }
            }
            adapter = galleryAdapter
        }
        viewModel.items.observe(this, {
            galleryAdapter.submitList(it)
            binding.beagleTextView.visible = it.isEmpty()
        })
        viewModel.shouldShowLoadingIndicator.observe(this) { binding.beagleProgressBar.visible = it }
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