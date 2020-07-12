package com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class AnalyticsAdapter(
    scope: CoroutineScope,
    private val onSwitchToggled: (Int, Boolean) -> Unit,
    private val onClearButtonPressed: () -> Unit
) : BaseAdapter<AnalyticsListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is SwitchViewHolder.UiModel -> R.layout.item_analytics_switch
        is ClearButtonViewHolder.UiModel -> R.layout.item_analytics_clear_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_analytics_switch -> SwitchViewHolder.create(parent, onSwitchToggled)
        R.layout.item_analytics_clear_button -> ClearButtonViewHolder.create(parent, onClearButtonPressed)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is SwitchViewHolder -> holder.bind(getItem(position) as SwitchViewHolder.UiModel)
        is ClearButtonViewHolder -> holder.bind(getItem(position) as ClearButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}