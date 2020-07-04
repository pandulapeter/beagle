package com.pandulapeter.beagle.appDemo.feature.main.inspiration.simpleSetup.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import kotlinx.coroutines.CoroutineScope

class SimpleSetupAdapter(
    scope: CoroutineScope,
    onSectionHeaderSelected: (SectionHeaderViewHolder.UiModel) -> Unit
) : BaseAdapter<SimpleSetupListItem>(scope, onSectionHeaderSelected) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is LoadingIndicatorViewHolder.UiModel -> R.layout.item_simple_setup_loading_indicator
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_simple_setup_loading_indicator -> LoadingIndicatorViewHolder.create(parent)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is LoadingIndicatorViewHolder -> holder.bind(getItem(position) as LoadingIndicatorViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}