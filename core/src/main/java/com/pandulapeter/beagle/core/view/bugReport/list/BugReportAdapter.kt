package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class BugReportAdapter(
    private val onMediaFileSelected: (String) -> Unit,
    private val onMediaFileLongTapped: (String) -> Unit,
    private val onNetworkLogSelected: (String) -> Unit,
    private val onNetworkLogLongTapped: (String) -> Unit,
    private val onShowMoreNetworkLogsTapped: () -> Unit,
    private val onLogSelected: (String, String?) -> Unit,
    private val onLogLongTapped: (String, String?) -> Unit,
    private val onShowMoreLogsTapped: (String?) -> Unit,
    private val onDescriptionChanged: (CharSequence) -> Unit
) : ListAdapter<BugReportListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<BugReportListItem>() {

    override fun areItemsTheSame(oldItem: BugReportListItem, newItem: BugReportListItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BugReportListItem, newItem: BugReportListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: BugReportListItem, newItem: BugReportListItem) = ""
}) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderViewHolder.UiModel -> R.layout.beagle_item_bug_report_header
        is GalleryViewHolder.UiModel -> R.layout.beagle_item_bug_report_gallery
        is NetworkLogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_network_log_item
        is LogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_log_item
        is ShowMoreNetworkLogsViewHolder.UiModel -> R.layout.beagle_item_bug_report_show_more_network_logs
        is ShowMoreLogsViewHolder.UiModel -> R.layout.beagle_item_bug_report_show_more_logs
        is DescriptionViewHolder.UiModel -> R.layout.beagle_item_bug_report_description
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_bug_report_header -> HeaderViewHolder.create(parent)
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
        R.layout.beagle_item_bug_report_show_more_logs -> ShowMoreLogsViewHolder.create(
            parent = parent,
            onButtonPressed = onShowMoreLogsTapped
        )
        R.layout.beagle_item_bug_report_description -> DescriptionViewHolder.create(
            parent = parent,
            onTextChanged = onDescriptionChanged
        )
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewHolder.UiModel)
        is GalleryViewHolder -> holder.bind(getItem(position) as GalleryViewHolder.UiModel)
        is NetworkLogItemViewHolder -> holder.bind(getItem(position) as NetworkLogItemViewHolder.UiModel)
        is LogItemViewHolder -> holder.bind(getItem(position) as LogItemViewHolder.UiModel)
        is ShowMoreNetworkLogsViewHolder -> Unit
        is ShowMoreLogsViewHolder -> holder.bind(getItem(position) as ShowMoreLogsViewHolder.UiModel)
        is DescriptionViewHolder -> holder.bind(getItem(position) as DescriptionViewHolder.UiModel)
        else -> throw IllegalArgumentException("Unsupported view holder type at position $position.")
    }
}