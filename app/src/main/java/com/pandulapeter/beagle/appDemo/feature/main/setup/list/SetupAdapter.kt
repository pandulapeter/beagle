package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class SetupAdapter(
    private val onGitHubButtonClicked: () -> Unit
) : BaseAdapter<SetupListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ButtonViewHolder.UiModel -> R.layout.item_inspiration_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_inspiration_button -> ButtonViewHolder.create(parent, onGitHubButtonClicked)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is ButtonViewHolder -> holder.bind(getItem(position) as ButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}