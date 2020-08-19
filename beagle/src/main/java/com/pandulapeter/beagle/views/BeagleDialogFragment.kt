package com.pandulapeter.beagle.views

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagle.utils.setBackgroundFromWindowBackground
import com.pandulapeter.beagle.utils.withArguments
import com.pandulapeter.beagleCore.configuration.Appearance

internal class BeagleDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (arguments?.appearance?.themeResourceId?.let { ContextThemeWrapper(context, it) } ?: context)?.let { themedContext ->
            arguments?.title?.let { title ->
                arguments?.content?.let { content ->
                    return AlertDialog.Builder(themedContext).apply {
                        setView(TolerantScrollView(themedContext).apply {
                            setBackgroundFromWindowBackground()
                            isVerticalScrollBarEnabled = false
                            overScrollMode = View.OVER_SCROLL_NEVER
                            clipToPadding = false
                            if (arguments?.shouldWrapContent == true) {
                                addView(AppCompatTextView(themedContext).apply {
                                    text = SpannableString("$title\n\n$content").apply {
                                        setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                    }
                                    context.dimension(R.dimen.beagle_large_content_padding).let { padding -> setPadding(padding, padding, padding, padding) }
                                }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            } else {
                                addView(ChildHorizontalScrollView(themedContext).apply {
                                    isHorizontalScrollBarEnabled = false
                                    overScrollMode = View.OVER_SCROLL_NEVER
                                    clipToPadding = false
                                    addView(AppCompatTextView(themedContext).apply {
                                        text = SpannableString("$title\n\n$content").apply {
                                            setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                                        }
                                        context.dimension(R.dimen.beagle_large_content_padding).let { padding -> setPadding(padding, padding, padding, padding) }
                                    }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                            }
                        })
                    }.create()
                }
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private var Bundle.title by BundleArgumentDelegate.String("title")
        private var Bundle.content by BundleArgumentDelegate.String("content")
        private var Bundle.appearance by BundleArgumentDelegate.Parcelable<Appearance>("appearance")
        private var Bundle.shouldWrapContent by BundleArgumentDelegate.Boolean("shouldWrapContent")

        fun show(fragmentManager: FragmentManager, title: String, content: String, appearance: Appearance, shouldWrapContent: Boolean) = BeagleDialogFragment().withArguments {
            it.title = title
            it.content = content
            it.appearance = appearance
            it.shouldWrapContent = shouldWrapContent
        }.run { show(fragmentManager, tag) }
    }
}