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
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.extensions.dimension

internal class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { context ->
        AlertDialog.Builder(context).apply {
            if (arguments?.isHorizontalScrollEnabled == true) {
                setView(TolerantScrollView(context).apply {
                    isVerticalScrollBarEnabled = false
                    overScrollMode = View.OVER_SCROLL_NEVER
                    clipToPadding = false
                    addView(ChildHorizontalScrollView(context).apply {
                        isHorizontalScrollBarEnabled = false
                        overScrollMode = View.OVER_SCROLL_NEVER
                        clipToPadding = false
                        addView(AppCompatTextView(context).apply {
                            text = arguments?.content
                            context.dimension(R.dimen.beagle_large_content_padding).let { padding -> setPadding(padding, padding, padding, padding) }
                            setTextIsSelectable(true)
                        }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                })
            } else {
                setMessage(arguments?.content)
            }
        }.create()
    }

    companion object {
        private var Bundle.content by BundleArgumentDelegate.CharSequence("content")
        private var Bundle.isHorizontalScrollEnabled by BundleArgumentDelegate.Boolean("isHorizontalScrollEnabled")

        fun show(fragmentManager: FragmentManager, content: CharSequence, isHorizontalScrollEnabled: Boolean) = AlertDialogFragment().withArguments {
            it.content = content
            it.isHorizontalScrollEnabled = isHorizontalScrollEnabled
        }.run { show(fragmentManager, tag) }
    }
}