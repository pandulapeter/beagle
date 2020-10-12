package com.pandulapeter.beagle.core.view

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.createAndShareFile
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.viewModel
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable


//TODO: Add UI controls for showing / hiding event metadata
internal class NetworkLogDetailDialogFragment : DialogFragment() {

    private val viewModel by viewModel<NetworkLogDetailDialogViewModel>()
    private lateinit var appBar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var textView: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var shareButton: MenuItem
    private val scrollListener = ViewTreeObserver.OnScrollChangedListener { appBar.setLifted(scrollView.scrollY != 0) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext().applyTheme())
        .setView(R.layout.beagle_dialog_fragment_network_log_detail)
        .create()

    override fun onResume() {
        super.onResume()
        dialog?.let { dialog ->
            appBar = dialog.findViewById(R.id.beagle_app_bar)
            toolbar = dialog.findViewById(R.id.beagle_toolbar)
            textView = dialog.findViewById(R.id.beagle_text_view)
            scrollView = dialog.findViewById(R.id.beagle_scroll_view)
            progressBar = dialog.findViewById(R.id.beagle_progress_bar)
            appBar.run {
                setPadding(0, 0, 0, 0)
                setBackgroundColor(context.colorResource(R.attr.colorBackgroundFloating))
            }
            scrollView.viewTreeObserver.addOnScrollChangedListener(scrollListener)
            toolbar.run {
                val textColor = context.colorResource(android.R.attr.textColorPrimary)
                setNavigationOnClickListener { dismiss() }
                navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
                shareButton = menu.findItem(R.id.beagle_share).also {
                    it.title = context.text(BeagleCore.implementation.appearance.generalTexts.shareHint)
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
                }
                setOnMenuItemClickListener(::onMenuItemClicked)
            }
            viewModel.isProgressBarVisible.observe(this, { progressBar.visible = it })
            viewModel.formattedJson.observe(this, { textView.text = it })
            if (viewModel.isProgressBarVisible.value == true) {
                viewModel.formatJson(
                    isOutgoing = arguments?.isOutgoing == true,
                    url = arguments?.url.orEmpty(),
                    headers = arguments?.headers.orEmpty(),
                    timestamp = arguments?.timestamp,
                    duration = arguments?.duration,
                    payload = arguments?.payload.orEmpty()
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        scrollView.viewTreeObserver.removeOnScrollChangedListener(scrollListener)
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume(::shareLogs)
        else -> false
    }

    private fun shareLogs() {
        textView.text?.let { text ->
            activity?.createAndShareFile("${BeagleCore.implementation.behavior.getNetworkLogFileName(arguments?.timestamp ?: 0L)}.txt", text.toString())
        }
    }

    companion object {
        private var Bundle.isOutgoing by BundleArgumentDelegate.Boolean("isOutgoing")
        private var Bundle.url by BundleArgumentDelegate.String("url")
        private var Bundle.payload by BundleArgumentDelegate.String("payload")
        private var Bundle.headers by BundleArgumentDelegate.StringList("headers")
        private var Bundle.duration by BundleArgumentDelegate.Long("duration")
        private var Bundle.timestamp by BundleArgumentDelegate.Long("timestamp")

        fun show(
            fragmentManager: FragmentManager,
            isOutgoing: Boolean,
            url: String,
            payload: String,
            headers: List<String>?,
            duration: Long?,
            timestamp: Long
        ) = NetworkLogDetailDialogFragment().withArguments {
            it.isOutgoing = isOutgoing
            it.url = url
            it.payload = payload
            it.headers = headers.orEmpty()
            it.duration = duration ?: -1L
            it.timestamp = timestamp
        }.run { show(fragmentManager, tag) }
    }
}