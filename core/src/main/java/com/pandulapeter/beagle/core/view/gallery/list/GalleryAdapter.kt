package com.pandulapeter.beagle.core.view.gallery.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class GalleryAdapter(
    private val onMediaSelected: (Int) -> Unit,
    private val onLongTap: (Int) -> Unit
) : ListAdapter<GalleryListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<GalleryListItem>() {

    override fun areItemsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem) = oldItem == newItem
}) {
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ImageViewHolder.UiModel -> R.layout.beagle_item_gallery_image
        is VideoViewHolder.UiModel -> R.layout.beagle_item_gallery_video
        is SectionHeaderViewHolder.UiModel -> R.layout.beagle_item_gallery_section_header
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.beagle_item_gallery_image -> ImageViewHolder.create(parent, onMediaSelected, onLongTap)
        R.layout.beagle_item_gallery_video -> VideoViewHolder.create(parent, onMediaSelected, onLongTap)
        R.layout.beagle_item_gallery_section_header -> SectionHeaderViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> holder.bind(getItem(position) as ImageViewHolder.UiModel)
            is VideoViewHolder -> holder.bind(getItem(position) as VideoViewHolder.UiModel)
            is SectionHeaderViewHolder -> holder.bind(getItem(position) as SectionHeaderViewHolder.UiModel)
        }
    }
}