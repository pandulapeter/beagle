package com.pandulapeter.beagle.appDemo.feature.main.home.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.utils.ListItem
import com.pandulapeter.beagle.appDemo.utils.ViewHolder

class HomeAdapter : ListAdapter<HomeListItem, ViewHolder<*, *>>(ListItem.DiffCallback()) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HintViewHolder.UiModel -> R.layout.item_home_hint
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*, *> = when (viewType) {
        R.layout.item_home_hint -> HintViewHolder.create(parent)
        else -> throw  IllegalArgumentException("Unsupported item view type: $viewType.")
    }

    override fun onBindViewHolder(holder: ViewHolder<*, *>, position: Int) = when (holder) {
        is HintViewHolder -> holder.bind(getItem(position) as HintViewHolder.UiModel)
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }
}