package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class NetworkLogDetailAdapter(
    private val onItemClicked: (Int) -> Unit
) : ListAdapter<NetworkLogDetailListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<NetworkLogDetailListItem>() {

    override fun areItemsTheSame(oldItem: NetworkLogDetailListItem, newItem: NetworkLogDetailListItem) = oldItem.lineIndex == newItem.lineIndex

    override fun areContentsTheSame(oldItem: NetworkLogDetailListItem, newItem: NetworkLogDetailListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: NetworkLogDetailListItem, newItem: NetworkLogDetailListItem) = ""
}) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is DetailsViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_details
        is LineViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_line
        is TitleViewHolder.UiModel -> R.layout.beagle_item_network_log_detail_title
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_network_log_detail_details -> DetailsViewHolder.create(parent)
        R.layout.beagle_item_network_log_detail_line -> LineViewHolder.create(parent, onItemClicked)
        R.layout.beagle_item_network_log_detail_title -> TitleViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is DetailsViewHolder -> holder.bind(getItem(position) as DetailsViewHolder.UiModel)
        is LineViewHolder -> holder.bind(getItem(position) as LineViewHolder.UiModel)
        is TitleViewHolder -> holder.bind(getItem(position) as TitleViewHolder.UiModel)
        else -> throw IllegalArgumentException("Unsupported view holder type at position $position.")
    }
}