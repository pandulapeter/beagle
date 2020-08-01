package com.pandulapeter.beagle.core.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.withArguments

internal class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context).apply {
            if (arguments?.getBoolean(IS_HORIZONTAL_SCROLL_ENABLED) == true) {
                setView(TolerantScrollView(context).apply {
                    isVerticalScrollBarEnabled = false
                    overScrollMode = View.OVER_SCROLL_NEVER
                    clipToPadding = false
                    addView(ChildHorizontalScrollView(context).apply {
                        isHorizontalScrollBarEnabled = false
                        overScrollMode = View.OVER_SCROLL_NEVER
                        clipToPadding = false
                        addView(AppCompatTextView(context).apply {
                            text = arguments?.getCharSequence(CONTENT)
                            context.dimension(R.dimen.beagle_large_content_padding).let { padding -> setPadding(padding, padding, padding, padding) }
                            setTextIsSelectable(true)
                        }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                })
            } else {
                setMessage(arguments?.getCharSequence(CONTENT))
            }
        }.create()
    }

    companion object {
        private const val CONTENT = "content"
        private const val IS_HORIZONTAL_SCROLL_ENABLED = "isHorizontalScrollEnabled"

        fun show(fragmentManager: FragmentManager, content: CharSequence, isHorizontalScrollEnabled: Boolean) = AlertDialogFragment().withArguments {
            it.putCharSequence(CONTENT, content)
            it.putBoolean(IS_HORIZONTAL_SCROLL_ENABLED, isHorizontalScrollEnabled)
        }.run { show(fragmentManager, tag) }
    }
}