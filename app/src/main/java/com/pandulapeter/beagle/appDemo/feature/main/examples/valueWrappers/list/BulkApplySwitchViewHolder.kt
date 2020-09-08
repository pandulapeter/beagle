package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemValueWrappersBulkApplySwitchBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class BulkApplySwitchViewHolder private constructor(
    binding: ItemValueWrappersBulkApplySwitchBinding,
    onStateChanged: (Boolean) -> Unit
) : BaseViewHolder<ItemValueWrappersBulkApplySwitchBinding, BulkApplySwitchViewHolder.UiModel>(binding) {

    init {
        binding.bulkApplySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && isChecked != binding.uiModel?.isChecked) {
                onStateChanged(isChecked)
            }
        }
    }

    data class UiModel(
        val isChecked: Boolean
    ) : ValueWrappersListItem {

        override val id: String = "bulkApplySwitch"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onStateChanged: (Boolean) -> Unit
        ) = BulkApplySwitchViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_value_wrappers_bulk_apply_switch, parent, false),
            onStateChanged = onStateChanged
        )
    }
}