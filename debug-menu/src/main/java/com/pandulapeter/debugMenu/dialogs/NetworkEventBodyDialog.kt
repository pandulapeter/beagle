package com.pandulapeter.debugMenu.dialogs

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.NetworkLogItem
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.dimension
import com.pandulapeter.debugMenu.utils.setBackgroundFromWindowBackground
import com.pandulapeter.debugMenu.utils.withArguments
import com.pandulapeter.debugMenuCore.configuration.UiCustomization

internal class NetworkEventBodyDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (arguments?.uiConfiguration?.themeResourceId?.let { ContextThemeWrapper(context, it) } ?: context)?.let { themedContext ->
            arguments?.networkEvent?.let { networkEvent ->
                return AlertDialog.Builder(themedContext).apply {
                    setView(LinearLayout(themedContext).apply {
                        setBackgroundFromWindowBackground()
                        orientation = LinearLayout.VERTICAL
                        val padding = context.dimension(R.dimen.large_content_padding)
                        addView(AppCompatTextView(themedContext).apply {
                            setTypeface(typeface, Typeface.BOLD)
                            setPadding(padding, padding, padding, 0)
                            text = "${networkEvent.url}${networkEvent.duration?.let { "\nDuration: $it ms" } ?: ""}"
                        }, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        addView(ScrollView(themedContext).apply {
                            setPadding(0, padding / 4, 0, padding)
                            clipToPadding = false
                            addView(HorizontalScrollView(themedContext).apply {
                                setPadding(padding, 0, padding, 0)
                                clipToPadding = false
                                val headers = if (arguments?.shouldShowHeaders == true) {
                                    "Headers:\n" +
                                            (if (networkEvent.headers.isEmpty()) "No headers" else networkEvent.headers.joinToString("\n")) +
                                            "\n\nPayload:\n"
                                } else ""
                                addView(AppCompatTextView(themedContext).apply {
                                    text = headers + networkEvent.body.run { if (isEmpty()) "No payload" else this }
                                }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                isHorizontalScrollBarEnabled = false
                                overScrollMode = View.OVER_SCROLL_NEVER
                            }, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            isVerticalScrollBarEnabled = false
                            overScrollMode = View.OVER_SCROLL_NEVER
                        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding })
                    })
                }.create()
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private var Bundle.networkEvent by BundleArgumentDelegate.Parcelable<NetworkLogItem>("networkLogItem")
        private var Bundle.uiConfiguration by BundleArgumentDelegate.Parcelable<UiCustomization>("uiConfiguration")
        private var Bundle.shouldShowHeaders by BundleArgumentDelegate.Boolean("shouldShowHeaders")

        fun show(fragmentManager: FragmentManager, networkLogItem: NetworkLogItem, uiCustomization: UiCustomization, shouldShowHeaders: Boolean) =
            NetworkEventBodyDialog().withArguments {
                it.networkEvent = networkLogItem
                it.uiConfiguration = uiCustomization
                it.shouldShowHeaders = shouldShowHeaders
            }.run { show(fragmentManager, tag) }
    }
}