package com.pandulapeter.beagle.core.view.gallery

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.BundleArgumentDelegate

internal class DeleteConfirmationDialogFragment : DialogFragment() {

    interface OnPositiveButtonClickedListener {

        fun onPositiveButtonClicked()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context)
            .setMessage(context.text(if (arguments?.isPlural == true) BeagleCore.implementation.appearance.galleryTexts.deleteConfirmationMessagePlural else BeagleCore.implementation.appearance.galleryTexts.deleteConfirmationMessageSingular))
            .setPositiveButton(context.text(BeagleCore.implementation.appearance.galleryTexts.deleteConfirmationPositive)) { _, _ ->
                ((parentFragment as? OnPositiveButtonClickedListener?) ?: (activity as? OnPositiveButtonClickedListener?))?.onPositiveButtonClicked()
                dismiss()
            }
            .setNegativeButton(context.text(BeagleCore.implementation.appearance.galleryTexts.deleteConfirmationNegative)) { _, _ -> dismiss() }
            .create()
    }

    companion object {
        private var Bundle.isPlural by BundleArgumentDelegate.Boolean("isPlural")

        fun show(fragmentManager: FragmentManager, isPlural: Boolean) = DeleteConfirmationDialogFragment().withArguments {
            it.isPlural = isPlural
        }.run { show(fragmentManager, tag) }
    }
}