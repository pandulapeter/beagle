package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class BugReportAdapter(
    private val onSendButtonPressed: () -> Unit,
    private val onMediaFileSelected: (String) -> Unit,
    private val onMediaFileLongTapped: (String) -> Unit,
    private val onNetworkLogSelected: (String) -> Unit,
    private val onNetworkLogLongTapped: (String) -> Unit,
    private val onShowMoreNetworkLogsTapped: () -> Unit,
    private val onLogSelected: (String, String?) -> Unit,
    private val onLogLongTapped: (String, String?) -> Unit,
) : ListAdapter<BugReportListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<BugReportListItem>() {

    override fun areItemsTheSame(oldItem: BugReportListItem, newItem: BugReportListItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BugReportListItem, newItem: BugReportListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: BugReportListItem, newItem: BugReportListItem) = ""
}) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderViewHolder.UiModel -> R.layout.beagle_item_bug_report_header
        is SendButtonViewHolder.UiModel -> R.layout.beagle_item_bug_report_send_button
        is GalleryViewHolder.UiModel -> R.layout.beagle_item_bug_report_gallery
        is NetworkLogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_network_log_item
        is LogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_log_item
        is ShowMoreNetworkLogsViewHolder.UiModel -> R.layout.beagle_item_bug_report_show_more_network_logs
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_bug_report_header -> HeaderViewHolder.create(parent)
        R.layout.beagle_item_bug_report_send_button -> SendButtonViewHolder.create(
            parent = parent,
            onSendButtonPressed = onSendButtonPressed
        )
        R.layout.beagle_item_bug_report_gallery -> GalleryViewHolder.create(
            parent = parent,
            onMediaSelected = onMediaFileSelected,
            onMediaLongTapped = onMediaFileLongTapped
        )
        R.layout.beagle_item_bug_report_network_log_item -> NetworkLogItemViewHolder.create(
            parent = parent,
            onItemSelected = onNetworkLogSelected,
            onItemLongTapped = onNetworkLogLongTapped
        )
        R.layout.beagle_item_bug_report_log_item -> LogItemViewHolder.create(
            parent = parent,
            onItemSelected = onLogSelected,
            onItemLongTapped = onLogLongTapped
        )
        R.layout.beagle_item_bug_report_show_more_network_logs -> ShowMoreNetworkLogsViewHolder.create(
            parent = parent,
            onButtonPressed = onShowMoreNetworkLogsTapped
        )
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewHolder.UiModel)
        is SendButtonViewHolder -> holder.bind(getItem(position) as SendButtonViewHolder.UiModel)
        is GalleryViewHolder -> holder.bind(getItem(position) as GalleryViewHolder.UiModel)
        is NetworkLogItemViewHolder -> holder.bind(getItem(position) as NetworkLogItemViewHolder.UiModel)
        is LogItemViewHolder -> holder.bind(getItem(position) as LogItemViewHolder.UiModel)
        is ShowMoreNetworkLogsViewHolder -> Unit
        else -> throw IllegalArgumentException("Unsupported view holder type at position $position.")
    }
}