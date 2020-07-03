package com.pandulapeter.beagle.core.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.withArguments

internal class AlertDialogFragment : DialogFragment() {

    //TODO: Handle isHorizontalScrollEnabled = true
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext().applyTheme())
        .setMessage(arguments?.getCharSequence(CONTENT))
        .create()

    companion object {
        private const val CONTENT = "content"
        private const val IS_HORIZONTAL_SCROLL_ENABLED = "isHorizontalScrollEnabled"

        fun show(fragmentManager: FragmentManager, content: CharSequence, isHorizontalScrollEnabled: Boolean) = AlertDialogFragment().withArguments {
            it.putCharSequence(CONTENT, content)
            it.putBoolean(IS_HORIZONTAL_SCROLL_ENABLED, isHorizontalScrollEnabled)
        }.run { show(fragmentManager, tag) }
    }
}