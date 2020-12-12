package com.pandulapeter.beagle.core.view.bugReport.list.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class BugReportGalleryAdapter(
    private val onMediaSelected: (String) -> Unit,
    private val onLongTap: (String) -> Unit
) : ListAdapter<BugReportGalleryListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<BugReportGalleryListItem>() {

    override fun areItemsTheSame(oldItem: BugReportGalleryListItem, newItem: BugReportGalleryListItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BugReportGalleryListItem, newItem: BugReportGalleryListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: BugReportGalleryListItem, newItem: BugReportGalleryListItem) = ""
}) {
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is BugReportImageViewHolder.UiModel -> R.layout.beagle_item_bug_report_gallery_image
        is BugReportVideoViewHolder.UiModel -> R.layout.beagle_item_bug_report_gallery_video
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_bug_report_gallery_image -> BugReportImageViewHolder.create(parent, onMediaSelected, onLongTap)
        R.layout.beagle_item_bug_report_gallery_video -> BugReportVideoViewHolder.create(parent, onMediaSelected, onLongTap)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BugReportImageViewHolder -> holder.bind(getItem(position) as BugReportImageViewHolder.UiModel)
            is BugReportVideoViewHolder -> holder.bind(getItem(position) as BugReportVideoViewHolder.UiModel)
        }
    }
}