package com.pandulapeter.debugMenu.dialogs

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.LogItem
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.dimension
import com.pandulapeter.debugMenu.utils.withArguments
import com.pandulapeter.debugMenuCore.configuration.UiCustomization

internal class LogPayloadDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (arguments?.uiConfiguration?.themeResourceId?.let { ContextThemeWrapper(context, it) } ?: context)?.let { themedContext ->
            arguments?.logMessage?.let { logMessage ->
                return AlertDialog.Builder(themedContext).apply {
                    setView(LinearLayout(themedContext).apply {
                        orientation = LinearLayout.VERTICAL
                        val padding = context.dimension(R.dimen.large_content_padding)
                        addView(AppCompatTextView(themedContext).apply {
                            setPadding(padding, padding, padding, 0)
                            setTypeface(typeface, Typeface.BOLD)
                            text = logMessage.message
                        }, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        addView(ScrollView(themedContext).apply {
                            isHorizontalScrollBarEnabled = false
                            overScrollMode = View.OVER_SCROLL_NEVER
                            setPadding(0, padding / 4, 0, padding)
                            clipToPadding = false
                            addView(AppCompatTextView(themedContext).apply {
                                text = logMessage.payload
                                setPadding(padding, 0, padding, 0)
                            }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding })
                    })
                }.create()
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private var Bundle.logMessage by BundleArgumentDelegate.Parcelable<LogItem>("logItem")
        private var Bundle.uiConfiguration by BundleArgumentDelegate.Parcelable<UiCustomization>("uiConfiguration")

        fun show(fragmentManager: FragmentManager, logItem: LogItem, uiCustomization: UiCustomization) = LogPayloadDialog().withArguments {
            it.logMessage = logItem
            it.uiConfiguration = uiCustomization
        }.run { show(fragmentManager, tag) }
    }
}