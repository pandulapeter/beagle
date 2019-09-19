package com.pandulapeter.debugMenu.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.utils.BundleArgumentDelegate
import com.pandulapeter.debugMenu.utils.dimension
import com.pandulapeter.debugMenu.utils.withArguments

internal class NetworkEventBodyDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            arguments?.networkEvent?.let { networkEvent ->
                return AlertDialog.Builder(it)
                    .setTitle("Event payload")
                    .setMessage(if (networkEvent.body.isEmpty()) "No payload for ${networkEvent.url}" else "Payload for ${networkEvent.url}${networkEvent.duration?.let { "\nDuration: $it ms" }
                        ?: ""}").apply {
                        if (networkEvent.body.isNotEmpty()) {
                            setView(HorizontalScrollView(context).apply {
                                addView(AppCompatTextView(context).apply {
                                    text = networkEvent.body
                                    context.dimension(R.dimen.large_content_padding).let { firstKeyline -> setPadding(firstKeyline, 0, firstKeyline / 2, firstKeyline) }
                                    overScrollMode = View.OVER_SCROLL_NEVER
                                }, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                                overScrollMode = HorizontalScrollView.OVER_SCROLL_NEVER
                                scrollBarStyle = HorizontalScrollView.SCROLLBARS_INSIDE_OVERLAY
                            })
                        }
                    }
                    .create()
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private var Bundle.networkEvent by BundleArgumentDelegate.Parcelable<NetworkEvent>("networkEvent")

        fun show(fragmentManager: FragmentManager, networkEvent: NetworkEvent) = NetworkEventBodyDialog().withArguments {
            it.networkEvent = networkEvent
        }.run { show(fragmentManager, tag) }
    }
}