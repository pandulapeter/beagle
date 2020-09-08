package com.pandulapeter.beagle.core.view.gallery.preview

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import coil.load
import coil.request.ImageRequest
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.core.view.gallery.GalleryActivity
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import com.pandulapeter.beagle.utils.extensions.waitForPreDraw
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MediaPreviewDialogFragment : DialogFragment() {

    private val fileName by lazy { arguments?.getString(FILE_NAME).orEmpty() }
    private val toolbar get() = dialog?.findViewById<Toolbar>(R.id.beagle_toolbar)
    private val imageView get() = dialog?.findViewById<ImageView>(R.id.beagle_image_view)
    private val videoView get() = dialog?.findViewById<VideoView>(R.id.beagle_video_view)
    private lateinit var shareButton: MenuItem
    private lateinit var deleteButton: MenuItem

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context).setView(R.layout.beagle_dialog_fragment_media_preview)
    }.create()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        context?.let { context ->
            imageView?.run {
                if (fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION)) {
                    load(context.getScreenCapturesFolder().resolve(fileName)) {
                        listener { _, _ -> setDialogSizeFromImage(this@run) }
                    }
                } else {
                    GlobalScope.launch {
                        BeagleCore.implementation.videoThumbnailLoader.execute(
                            ImageRequest.Builder(context)
                                .data(context.getScreenCapturesFolder().resolve(fileName))
                                .target(this@run)
                                .listener { _, _ -> setDialogSizeFromImage(this@run) }
                                .build()
                        )
                    }
                }
            }
            toolbar?.run {
                val textColor = context.colorResource(android.R.attr.textColorPrimary)
                setNavigationOnClickListener { dismiss() }
                navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
                shareButton = menu.findItem(R.id.beagle_share).also {
                    //TODO: Menu item hint should be configurable.
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
                }
                deleteButton = menu.findItem(R.id.beagle_delete).also {
                    //TODO: Menu item hint should be configurable.
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_delete, textColor)
                }
                setOnMenuItemClickListener(::onMenuItemClicked)
            }
        }
    }

    private fun setDialogSizeFromImage(imageView: ImageView) {
        imageView.run {
            waitForPreDraw {
                dialog?.window?.setLayout(width, height + (toolbar?.height ?: 0))
                waitForPreDraw {
                    visible = true
                    if (fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION)) {
                        videoView?.run {
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
        R.id.beagle_share -> consume { shareItem() }
        R.id.beagle_delete -> consume { deleteItem() }
        else -> false
    }

    private fun shareItem() {
        activity?.run {
            val uri = getUriForFile(getScreenCapturesFolder().resolve(fileName))
            when {
                fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> shareFile(uri, ScreenCaptureManager.IMAGE_TYPE)
                fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> shareFile(uri, ScreenCaptureManager.VIDEO_TYPE)
            }
            dismiss()
        }
    }

    private fun deleteItem() {
        //TODO: Confirmation dialog
        GlobalScope.launch {
            activity?.run {
                getScreenCapturesFolder().resolve(fileName).delete()
                (this as? GalleryActivity)?.loadMedia()
            }
        }
        dismiss()
    }

    companion object {
        private const val FILE_NAME = "fileName"
        const val TAG = "beagleMediaPreviewDialogFragment"

        fun show(fragmentManager: FragmentManager, fileName: String) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                MediaPreviewDialogFragment().withArguments {
                    it.putString(FILE_NAME, fileName)
                }.run { show(fragmentManager, TAG) }
            }
        }
    }
}