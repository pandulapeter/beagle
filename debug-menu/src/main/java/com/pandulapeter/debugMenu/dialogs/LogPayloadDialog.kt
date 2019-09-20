package com.pandulapeter.debugMenu.dialogs

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.dimension
import com.pandulapeter.debugMenu.utils.setBackground
import com.pandulapeter.debugMenu.utils.withArguments
import com.pandulapeter.debugMenuCore.configuration.UiConfiguration

internal class LogPayloadDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            arguments?.logMessage?.let { logMessage ->
                return AlertDialog.Builder(it).apply {
                    setView(LinearLayout(context).apply {
                        arguments?.uiConfiguration?.let { setBackground(it) }
                        orientation = LinearLayout.VERTICAL
                        val padding = context.dimension(R.dimen.large_content_padding)
                        setPadding(padding, padding, padding, padding)
                        addView(AppCompatTextView(context).apply {
                            setTypeface(typeface, Typeface.BOLD)
                            text = logMessage.message
                            setTextColor(DebugMenu.textColor)
                        }, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        addView(ScrollView(context).apply {
                            addView(AppCompatTextView(context).apply {
                                text = logMessage.payload
                                setTextColor(DebugMenu.textColor)
                            }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding })
                    })
                }.create()
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private var Bundle.logMessage by BundleArgumentDelegate.Parcelable<LogMessage>("logMessage")
        private var Bundle.uiConfiguration by BundleArgumentDelegate.Parcelable<UiConfiguration>("uiConfiguration")

        fun show(fragmentManager: FragmentManager, logMessage: LogMessage, uiConfiguration: UiConfiguration) = LogPayloadDialog().withArguments {
            it.logMessage = logMessage
            it.uiConfiguration = uiConfiguration
        }.run { show(fragmentManager, tag) }
    }
}