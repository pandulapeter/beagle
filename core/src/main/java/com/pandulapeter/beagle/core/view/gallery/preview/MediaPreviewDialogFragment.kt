package com.pandulapeter.beagle.core.view.gallery.preview

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
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
import com.pandulapeter.beagle.core.util.extension.withArguments
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MediaPreviewDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context).setView(R.layout.beagle_dialog_fragment_media_preview)
    }.create()

    override fun onResume() {
        super.onResume()
        val context = requireContext()
        dialog?.findViewById<ImageView>(R.id.beagle_image_view)?.apply {
            arguments?.getString(FILE_NAME)?.let { fileName ->
                if (fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION)) {
                    load(context.getScreenCapturesFolder().resolve(fileName)) {
                        listener { _, _ -> refreshSize(this@apply) }
                    }
                } else {
                    GlobalScope.launch {
                        //TODO: Display a video player instead.
                        BeagleCore.implementation.videoThumbnailLoader.execute(
                            ImageRequest.Builder(context)
                                .data(context.getScreenCapturesFolder().resolve(fileName))
                                .target(this@apply)
                                .listener { _, _ -> refreshSize(this@apply) }
                                .build()
                        )
                    }
                }
            }
        }
    }

    private fun refreshSize(imageView: ImageView) {
        imageView.post { dialog?.window?.setLayout(imageView.width, imageView.height) }
    }

    companion object {
        private const val FILE_NAME = "fileName"

        fun show(fragmentManager: FragmentManager, fileName: String) = MediaPreviewDialogFragment().withArguments {
            it.putString(FILE_NAME, fileName)
        }.run { show(fragmentManager, tag) }
    }
}