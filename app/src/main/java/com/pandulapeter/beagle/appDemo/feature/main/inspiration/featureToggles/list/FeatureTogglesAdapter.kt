package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class FeatureTogglesAdapter(
    scope: CoroutineScope,
    private val onResetButtonPressed: () -> Unit
) : BaseAdapter<FeatureTogglesListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CurrentStateViewHolder.UiModel -> R.layout.item_feature_toggles_current_state
        is ResetButtonViewHolder.UiModel -> R.layout.item_feature_toggles_reset_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_feature_toggles_current_state -> CurrentStateViewHolder.create(parent)
        R.layout.item_feature_toggles_reset_button -> ResetButtonViewHolder.create(parent, onResetButtonPressed)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is CurrentStateViewHolder -> holder.bind(getItem(position) as CurrentStateViewHolder.UiModel)
        is ResetButtonViewHolder -> holder.bind(getItem(position) as ResetButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}