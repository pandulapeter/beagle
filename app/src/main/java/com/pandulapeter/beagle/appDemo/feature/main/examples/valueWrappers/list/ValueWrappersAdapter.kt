package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import kotlinx.coroutines.CoroutineScope

class ValueWrappersAdapter(
    scope: CoroutineScope,
    onSectionHeaderSelected: (SectionHeaderViewHolder.UiModel) -> Unit,
    private val onCurrentStateCardPressed: () -> Unit,
    private val onResetButtonPressed: () -> Unit,
    private val onEnableAllModulesSwitchToggled: (Boolean) -> Unit,
    private val onBulkApplySwitchToggled: (Boolean) -> Unit
) : BaseAdapter<ValueWrappersListItem>(scope, onSectionHeaderSelected) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CurrentStateViewHolder.UiModel -> R.layout.item_value_wrappers_current_state
        is ResetButtonViewHolder.UiModel -> R.layout.item_value_wrappers_reset_button
        is EnableAllModulesSwitchViewHolder.UiModel -> R.layout.item_value_wrappers_enable_all_modules_switch
        is BulkApplySwitchViewHolder.UiModel -> R.layout.item_value_wrappers_bulk_apply_switch
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_value_wrappers_current_state -> CurrentStateViewHolder.create(parent, onCurrentStateCardPressed)
        R.layout.item_value_wrappers_reset_button -> ResetButtonViewHolder.create(parent, onResetButtonPressed)
        R.layout.item_value_wrappers_enable_all_modules_switch -> EnableAllModulesSwitchViewHolder.create(parent, onEnableAllModulesSwitchToggled)
        R.layout.item_value_wrappers_bulk_apply_switch -> BulkApplySwitchViewHolder.create(parent, onBulkApplySwitchToggled)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is CurrentStateViewHolder -> holder.bind(getItem(position) as CurrentStateViewHolder.UiModel)
        is ResetButtonViewHolder -> holder.bind(getItem(position) as ResetButtonViewHolder.UiModel)
        is EnableAllModulesSwitchViewHolder -> holder.bind(getItem(position) as EnableAllModulesSwitchViewHolder.UiModel)
        is BulkApplySwitchViewHolder -> holder.bind(getItem(position) as BulkApplySwitchViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}