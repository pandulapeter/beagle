package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.view.bugReport.BugReportViewModel

internal class BugReportAdapter(
    private val onMediaFileSelected: (String) -> Unit,
    private val onMediaFileLongTapped: (String) -> Unit,
    private val onCrashLogSelected: (String) -> Unit,
    private val onCrashLogLongTapped: (String) -> Unit,
    private val onNetworkLogSelected: (String) -> Unit,
    private val onNetworkLogLongTapped: (String) -> Unit,
    private val onLogSelected: (String, String?) -> Unit,
    private val onLogLongTapped: (String, String?) -> Unit,
    private val onLifecycleLogSelected: (String) -> Unit,
    private val onLifecycleLogLongTapped: (String) -> Unit,
    private val onShowMoreTapped: (ShowMoreViewHolder.Type) -> Unit,
    private val onDescriptionChanged: (Int, CharSequence) -> Unit,
    private val onMetadataItemClicked: (BugReportViewModel.MetadataType) -> Unit,
    private val onMetadataItemSelectionChanged: (BugReportViewModel.MetadataType) -> Unit
) : ListAdapter<BugReportListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<BugReportListItem>() {

    override fun areItemsTheSame(oldItem: BugReportListItem, newItem: BugReportListItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BugReportListItem, newItem: BugReportListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: BugReportListItem, newItem: BugReportListItem) = ""
}) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderViewHolder.UiModel -> R.layout.beagle_item_bug_report_header
        is GalleryViewHolder.UiModel -> R.layout.beagle_item_bug_report_gallery
        is CrashLogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_crash_log_item
        is NetworkLogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_network_log_item
        is LogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_log_item
        is LifecycleLogItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_lifecycle_log_item
        is ShowMoreViewHolder.UiModel -> R.layout.beagle_item_bug_report_show_more
        is DescriptionViewHolder.UiModel -> R.layout.beagle_item_bug_report_description
        is MetadataItemViewHolder.UiModel -> R.layout.beagle_item_bug_report_metadata_item
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_bug_report_header -> HeaderViewHolder.create(parent)
        R.layout.beagle_item_bug_report_gallery -> GalleryViewHolder.create(
            parent = parent,
            onMediaSelected = onMediaFileSelected,
            onMediaLongTapped = onMediaFileLongTapped
        )
        R.layout.beagle_item_bug_report_crash_log_item -> CrashLogItemViewHolder.create(
            parent = parent,
            onItemSelected = onCrashLogSelected,
            onItemLongTapped = onCrashLogLongTapped
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
        R.layout.beagle_item_bug_report_lifecycle_log_item -> LifecycleLogItemViewHolder.create(
            parent = parent,
            onItemSelected = onLifecycleLogSelected,
            onItemLongTapped = onLifecycleLogLongTapped
        )
        R.layout.beagle_item_bug_report_show_more -> ShowMoreViewHolder.create(
            parent = parent,
            onButtonPressed = onShowMoreTapped
        )
        R.layout.beagle_item_bug_report_description -> DescriptionViewHolder.create(
            parent = parent,
            onTextChanged = onDescriptionChanged
        )
        R.layout.beagle_item_bug_report_metadata_item -> MetadataItemViewHolder.create(
            parent = parent,
            onItemClicked = onMetadataItemClicked,
            onItemSelectionChanged = onMetadataItemSelectionChanged
        )
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewHolder.UiModel)
        is GalleryViewHolder -> holder.bind(getItem(position) as GalleryViewHolder.UiModel)
        is CrashLogItemViewHolder -> holder.bind(getItem(position) as CrashLogItemViewHolder.UiModel)
        is NetworkLogItemViewHolder -> holder.bind(getItem(position) as NetworkLogItemViewHolder.UiModel)
        is LogItemViewHolder -> holder.bind(getItem(position) as LogItemViewHolder.UiModel)
        is LifecycleLogItemViewHolder -> holder.bind(getItem(position) as LifecycleLogItemViewHolder.UiModel)
        is ShowMoreViewHolder -> holder.bind(getItem(position) as ShowMoreViewHolder.UiModel)
        is DescriptionViewHolder -> holder.bind(getItem(position) as DescriptionViewHolder.UiModel)
        is MetadataItemViewHolder -> holder.bind(getItem(position) as MetadataItemViewHolder.UiModel)
        else -> throw IllegalArgumentException("Unsupported view holder type at position $position.")
    }
}