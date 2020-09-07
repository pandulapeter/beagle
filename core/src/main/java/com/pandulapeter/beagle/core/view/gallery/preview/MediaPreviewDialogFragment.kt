package com.pandulapeter.beagle.core.view.gallery.preview

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.withArguments

class MediaPreviewDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context).apply {
            //setView(...)
            setMessage(arguments?.getString(FILE_NAME))
        }
    }.create()

    companion object {
        private const val FILE_NAME = "fileName"

        fun show(fragmentManager: FragmentManager, fileName: String) = MediaPreviewDialogFragment().withArguments {
            it.putString(FILE_NAME, fileName)
        }.run { show(fragmentManager, tag) }
    }
}