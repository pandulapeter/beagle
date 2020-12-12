package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class NetworkLogDetailAdapter(
    private val onHeaderClicked: () -> Unit,
    private val onItemClicked: (Int) -> Unit
) : ListAdapter<NetworkLogDetailListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<NetworkLogDetailListItem>() {

    override fun areItemsTheSame(oldItem: NetworkLogDetailListItem, newItem: NetworkLogDetailListItem) = oldItem.lineIndex == newItem.lineIndex

    override fun areContentsTheSame(oldItem: NetworkLogDetailListItem, newItem: NetworkLogDetailListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: NetworkLogDetailListItem, newItem: NetworkLogDetailListItem) = ""
}) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is MetadataDetailsViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_metadata_details
        is MetadataHeaderViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_metadata_header
        is LineViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_line
        is TitleViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_title
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_network_log_detail_metadata_details -> MetadataDetailsViewHolder.create(parent)
        R.layout.beagle_item_network_log_detail_metadata_header -> MetadataHeaderViewHolder.create(parent, onHeaderClicked)
        R.layout.beagle_item_network_log_detail_line -> LineViewHolder.create(parent, onItemClicked)
        R.layout.beagle_item_network_log_detail_title -> TitleViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is MetadataDetailsViewHolder -> holder.bind(getItem(position) as MetadataDetailsViewHolder.UiModel)
        is MetadataHeaderViewHolder -> holder.bind(getItem(position) as MetadataHeaderViewHolder.UiModel)
        is LineViewHolder -> holder.bind(getItem(position) as LineViewHolder.UiModel)
        is TitleViewHolder -> holder.bind(getItem(position) as TitleViewHolder.UiModel)
        else -> throw IllegalArgumentException("Unsupported view holder type at position $position.")
    }
}