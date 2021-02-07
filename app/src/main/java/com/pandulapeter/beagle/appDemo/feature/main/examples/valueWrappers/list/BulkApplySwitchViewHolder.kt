package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemValueWrappersBulkApplySwitchBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

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
            binding = ItemValueWrappersBulkApplySwitchBinding.inflate(parent.inflater, parent, false),
            onStateChanged = onStateChanged
        )
    }
}