package com.pandulapeter.debugMenu.dialogs

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.dimension
import com.pandulapeter.debugMenu.utils.setBackground
import com.pandulapeter.debugMenu.utils.withArguments
import com.pandulapeter.debugMenuCore.configuration.UiConfiguration

internal class NetworkEventBodyDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            arguments?.networkEvent?.let { networkEvent ->
                return AlertDialog.Builder(it).apply {
                    setView(LinearLayout(context).apply {
                        arguments?.uiConfiguration?.let { setBackground(it) }
                        orientation = LinearLayout.VERTICAL
                        val padding = context.dimension(R.dimen.large_content_padding)
                        setPadding(padding, padding, padding, padding)
                        addView(AppCompatTextView(context).apply {
                            setTypeface(typeface, Typeface.BOLD)
                            text = "${networkEvent.url}${networkEvent.duration?.let { " (duration: $it ms)" } ?: ""}"
                            setTextColor(DebugMenu.textColor)
                        }, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        addView(AppCompatTextView(context).apply {
                            text = networkEvent.body.run { if (isEmpty()) "No payload" else this }
                            setTextColor(DebugMenu.textColor)
                            overScrollMode = View.OVER_SCROLL_NEVER
                        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = padding })
                    })
                }
                    .create()
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private var Bundle.networkEvent by BundleArgumentDelegate.Parcelable<NetworkEvent>("networkEvent")
        private var Bundle.uiConfiguration by BundleArgumentDelegate.Parcelable<UiConfiguration>("uiConfiguration")

        fun show(fragmentManager: FragmentManager, networkEvent: NetworkEvent, uiConfiguration: UiConfiguration) = NetworkEventBodyDialog().withArguments {
            it.networkEvent = networkEvent
            it.uiConfiguration = uiConfiguration
        }.run { show(fragmentManager, tag) }
    }
}