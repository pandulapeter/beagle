package com.pandulapeter.beagle.core.view.gallery

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import coil3.load
import coil3.request.ImageRequest
import coil3.target.ImageViewTarget
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleDialogFragmentMediaPreviewBinding
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import com.pandulapeter.beagle.utils.extensions.waitForPreDraw
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.max

class MediaPreviewDialogFragment : DialogFragment(), DeleteConfirmationDialogFragment.OnPositiveButtonClickedListener {

    private lateinit var binding: BeagleDialogFragmentMediaPreviewBinding
    private val fileName by lazy { arguments?.fileName.orEmpty() }
    private lateinit var shareButton: MenuItem
    private lateinit var deleteButton: MenuItem
    private var isLoaded = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { themedContext ->
        AlertDialog.Builder(themedContext)
            .setView(
                BeagleDialogFragmentMediaPreviewBinding.inflate(themedContext.inflater, null, false).also {
                    binding = it
                }.root
            )
            .create()
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            binding.beagleImageView.run {
                if (fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION)) {
                    load(context.getScreenCapturesFolder().resolve(fileName)) {
                        listener { _, _ -> setDialogSizeFromImage(this@run) }
                    }
                } else {
                    GlobalScope.launch {
                        BeagleCore.implementation.videoThumbnailLoader.execute(
                            ImageRequest.Builder(context)
                                .data(context.getScreenCapturesFolder().resolve(fileName))
                                .target(ImageViewTarget(this@run))
                                .listener { _, _ -> setDialogSizeFromImage(this@run) }
                                .build()
                        )
                    }
                }
            }
            binding.beagleToolbar.run {
                val textColor = context.colorResource(android.R.attr.textColorPrimary)
                setNavigationOnClickListener { dismiss() }
                navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
                shareButton = menu.findItem(R.id.beagle_share).also {
                    it.title = context.text(BeagleCore.implementation.appearance.generalTexts.shareHint)
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
                }
                deleteButton = menu.findItem(R.id.beagle_delete).also {
                    it.title = context.text(BeagleCore.implementation.appearance.galleryTexts.deleteHint)
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_delete, textColor)
                }
                setOnMenuItemClickListener(::onMenuItemClicked)
            }
        }
    }

    override fun onPositiveButtonClicked() {
        GlobalScope.launch {
            activity?.run {
                getScreenCapturesFolder().resolve(fileName).delete()
                (this as? GalleryActivity)?.loadMedia()
                (this as? BugReportActivity)?.refresh()
            }
        }
        dismiss()
    }

    private fun setDialogSizeFromImage(imageView: ImageView) {
        imageView.run {
            waitForPreDraw {
                dialog?.window?.let { window ->
                    val padding = context.dimension(R.dimen.beagle_content_padding)
                    if (window.decorView.width > width + padding * 8) {
                        window.setLayout(
                            max(width + padding * 4, context.dimension(R.dimen.beagle_gallery_preview_minimum_width)),
                            height + binding.beagleToolbar.height + padding
                        )
                    }
                }
                waitForPreDraw {
                    visible = true
                    isLoaded = true
                    if (fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION)) {
                        binding.beagleVideoView.run {
                            visible = true
                            setOnPreparedListener { it.isLooping = true }
                            setVideoPath(context.getScreenCapturesFolder().resolve(fileName).path)
                            start()
                        }
                    }
                }
            }
        }
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume(::shareItem)
        R.id.beagle_delete -> consume { DeleteConfirmationDialogFragment.show(childFragmentManager, false) }
        else -> false
    }

    private fun shareItem() {
        activity?.run {
            val uri = getUriForFile(getScreenCapturesFolder().resolve(fileName))
            when {
                fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> shareFile(uri, ScreenCaptureManager.IMAGE_TYPE)
                fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> shareFile(uri, ScreenCaptureManager.VIDEO_TYPE)
            }
        }
    }

    companion object {
        const val TAG = "beagleMediaPreviewDialogFragment"
        private var Bundle.fileName by BundleArgumentDelegate.String("fileName")

        fun show(fragmentManager: FragmentManager, fileName: String) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                MediaPreviewDialogFragment().withArguments {
                    it.fileName = fileName
                }.run { show(fragmentManager, TAG) }
            }
        }
    }
}