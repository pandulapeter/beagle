package com.pandulapeter.beagle.core.view.gallery.preview

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import coil.load
import coil.request.ImageRequest
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.extensions.waitForPreDraw
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MediaPreviewDialogFragment : DialogFragment() {

    private val fileName by lazy { arguments?.getString(FILE_NAME).orEmpty() }
    private val imageView get() = dialog?.findViewById<ImageView>(R.id.beagle_image_view)
    private val videoView get() = dialog?.findViewById<VideoView>(R.id.beagle_video_view)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context).setView(R.layout.beagle_dialog_fragment_media_preview)
    }.create()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        val context = requireContext()
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
    }

    private fun setDialogSizeFromImage(imageView: ImageView) {
        imageView.run {
            waitForPreDraw {
                dialog?.window?.setLayout(width, height)
                waitForPreDraw {
                    visible = true
                    if (fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION)) {
                        videoView?.run {
                            visible = true
                            setOnPreparedListener { mp -> mp.isLooping = true }
                            setVideoPath(context.getScreenCapturesFolder().resolve(fileName).path)
                            start()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val FILE_NAME = "fileName"
        private const val TAG = "beagleMediaPreviewDialogFragment"

        fun show(fragmentManager: FragmentManager, fileName: String) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                MediaPreviewDialogFragment().withArguments {
                    it.putString(FILE_NAME, fileName)
                }.run { show(fragmentManager, TAG) }
            }
        }
    }
}