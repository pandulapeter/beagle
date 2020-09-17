package com.pandulapeter.beagle.core.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.shareText
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class AlertDialogFragment : DialogFragment() {

    private val toolbar get() = dialog?.findViewById<Toolbar>(R.id.beagle_toolbar)
    private val textView get() = dialog?.findViewById<TextView>(R.id.beagle_text_view)
    private lateinit var shareButton: MenuItem

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext().applyTheme())
        .setView(if (arguments?.isHorizontalScrollEnabled == true) R.layout.beagle_dialog_fragment_alert_large else R.layout.beagle_dialog_fragment_alert_small)
        .create()

    override fun onResume() {
        super.onResume()
        textView?.text = arguments?.content
        toolbar?.run {
            val textColor = context.colorResource(android.R.attr.textColorPrimary)
            setNavigationOnClickListener { dismiss() }
            navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
            shareButton = menu.findItem(R.id.beagle_share).also {
                it.title = BeagleCore.implementation.appearance.galleryShareHint
                it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
            }
            setOnMenuItemClickListener(::onMenuItemClicked)
        }
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume { shareItem() }
        else -> false
    }

    private fun shareItem() {
        textView?.text?.let { text ->
            activity?.shareText(text.toString())
        }
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