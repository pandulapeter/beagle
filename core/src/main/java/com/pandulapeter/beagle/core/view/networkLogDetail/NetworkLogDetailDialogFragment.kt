package com.pandulapeter.beagle.core.view.networkLogDetail

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.jsonLevel
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.viewModel
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.extension.withArguments
import com.pandulapeter.beagle.core.view.networkLogDetail.list.NetworkLogDetailAdapter
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.tintedDrawable


internal class NetworkLogDetailDialogFragment : DialogFragment() {

    private val viewModel by viewModel<NetworkLogDetailDialogViewModel>()
    private lateinit var appBar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var longestLineText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var horizontalScrollView: View
    private lateinit var toggleDetailsButton: MenuItem
    private lateinit var shareButton: MenuItem
    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            appBar.setLifted(recyclerView.computeVerticalScrollOffset() != 0)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext().applyTheme())
        .setView(R.layout.beagle_dialog_fragment_network_log_detail)
        .create()

    override fun onResume() {
        super.onResume()
        dialog?.let { dialog ->
            appBar = dialog.findViewById(R.id.beagle_app_bar)
            toolbar = dialog.findViewById(R.id.beagle_toolbar)
            longestLineText = dialog.findViewById(R.id.beagle_longest_text_view)
            recyclerView = dialog.findViewById(R.id.beagle_recycler_view)
            progressBar = dialog.findViewById(R.id.beagle_progress_bar)
            horizontalScrollView = dialog.findViewById(R.id.beagle_child_horizontal_scroll_view)
            appBar.run {
                setPadding(0, 0, 0, 0)
                setBackgroundColor(context.colorResource(R.attr.colorBackgroundFloating))
            }
            recyclerView.addOnScrollListener(scrollListener)
            val textColor = dialog.context.colorResource(android.R.attr.textColorPrimary)
            toolbar.run {
                setNavigationOnClickListener { dismiss() }
                navigationIcon = context.tintedDrawable(R.drawable.beagle_ic_close, textColor)
                toggleDetailsButton = menu.findItem(R.id.beagle_toggle_details).also {
                    it.isVisible = true
                    it.title = context.text(BeagleCore.implementation.appearance.networkLogTexts.toggleExpandCollapseHint)
                }
                shareButton = menu.findItem(R.id.beagle_share).also {
                    it.title = context.text(BeagleCore.implementation.appearance.generalTexts.shareHint)
                    it.icon = context.tintedDrawable(R.drawable.beagle_ic_share, textColor)
                }
                setOnMenuItemClickListener(::onMenuItemClicked)
            }
            viewModel.isProgressBarVisible.observe(this, {
                progressBar.visible = it
                horizontalScrollView.visible = !it
            })
            viewModel.longestLine.observe(this, { line ->
                context?.let { context ->
                    val contentPadding = context.dimension(R.dimen.beagle_large_content_padding)
                    longestLineText.run {
                        text = line.trim()
                        setPadding(contentPadding * (line.jsonLevel + 1), paddingTop, contentPadding, paddingBottom)
                    }
                }
            })
            viewModel.areTagsExpanded.observe(this, {
                toggleDetailsButton.icon = context?.tintedDrawable(if (it) R.drawable.beagle_ic_toggle_details_on else R.drawable.beagle_ic_toggle_details_off, textColor)
            })
            val networkLogDetailAdapter = NetworkLogDetailAdapter(
                onHeaderClicked = viewModel::onHeaderClicked,
                onItemClicked = viewModel::onItemClicked
            )
            recyclerView.run {
                layoutManager = LinearLayoutManager(context)
                adapter = networkLogDetailAdapter
            }
            //TODO: Multiple observers are set
            viewModel.items.observe(this, networkLogDetailAdapter::submitList)
            viewModel.isShareButtonEnabled.observe(this, { shareButton.isEnabled = it })
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
        recyclerView.removeOnScrollListener(scrollListener)
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_share -> consume {
            viewModel.shareLogs(
                activity = activity,
                timestamp = arguments?.timestamp ?: 0L,
                id = arguments?.id.orEmpty()
            )
        }
        R.id.beagle_toggle_details -> consume(viewModel::onToggleDetailsButtonPressed)
        else -> false
    }

    companion object {
        private var Bundle.isOutgoing by BundleArgumentDelegate.Boolean("isOutgoing")
        private var Bundle.url by BundleArgumentDelegate.String("url")
        private var Bundle.payload by BundleArgumentDelegate.String("payload")
        private var Bundle.headers by BundleArgumentDelegate.StringList("headers")
        private var Bundle.duration by BundleArgumentDelegate.Long("duration")
        private var Bundle.timestamp by BundleArgumentDelegate.Long("timestamp")
        private var Bundle.id by BundleArgumentDelegate.String("id")

        fun show(
            fragmentManager: FragmentManager,
            isOutgoing: Boolean,
            url: String,
            payload: String,
            headers: List<String>?,
            duration: Long?,
            timestamp: Long,
            id: String
        ) = NetworkLogDetailDialogFragment().withArguments {
            it.isOutgoing = isOutgoing
            it.url = url
            it.payload = payload
            it.headers = headers.orEmpty()
            it.duration = duration ?: -1L
            it.timestamp = timestamp
            it.id = id
        }.run { show(fragmentManager, tag) }
    }
}