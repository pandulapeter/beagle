package com.pandulapeter.beagle.core.view.networkLogDetail

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleDialogFragmentNetworkLogDetailBinding
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
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class NetworkLogDetailDialogFragment : DialogFragment() {

    private lateinit var binding: BeagleDialogFragmentNetworkLogDetailBinding
    private val viewModel by viewModel<NetworkLogDetailDialogViewModel>()
    private lateinit var toggleDetailsButton: MenuItem
    private lateinit var shareButton: MenuItem
    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            binding.beagleAppBar.isLifted = recyclerView.computeVerticalScrollOffset() != 0
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = requireContext().applyTheme().let { themedContext ->
        AlertDialog.Builder(themedContext)
            .setView(
                BeagleDialogFragmentNetworkLogDetailBinding.inflate(themedContext.inflater, null, false).also {
                    binding = it
                }.root
            )
            .create()
    }

    override fun onResume() {
        super.onResume()
        dialog?.let { dialog ->
            binding.beagleAppBar.run {
                setPadding(0, 0, 0, 0)
                setBackgroundColor(context.colorResource(R.attr.colorBackgroundFloating))
            }
            binding.beagleRecyclerView.addOnScrollListener(scrollListener)
            val textColor = dialog.context.colorResource(android.R.attr.textColorPrimary)
            binding.beagleToolbar.run {
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
                binding.beagleProgressBar.visible = it
                if (!it) {
                    binding.beagleChildHorizontalScrollView.visible = true
                }
            })
            viewModel.longestLine.observe(this, { line ->
                context?.let { context ->
                    val contentPadding = context.dimension(R.dimen.beagle_large_content_padding)
                    binding.beagleLongestTextView.run {
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
            binding.beagleRecyclerView.run {
                layoutManager = LinearLayoutManager(context)
                adapter = networkLogDetailAdapter
            }
            //TODO: Multiple observers are set, viewLifecycleOwner should be used instead (but it is null...)
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
        binding.beagleRecyclerView.removeOnScrollListener(scrollListener)
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