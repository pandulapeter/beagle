package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemValueWrappersEnableAllModulesSwitchBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class EnableAllModulesSwitchViewHolder private constructor(
    binding: ItemValueWrappersEnableAllModulesSwitchBinding,
    onStateChanged: (Boolean) -> Unit
) : BaseViewHolder<ItemValueWrappersEnableAllModulesSwitchBinding, EnableAllModulesSwitchViewHolder.UiModel>(binding) {

    init {
        binding.enableAllModulesSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && isChecked != binding.uiModel?.isChecked) {
                onStateChanged(isChecked)
            }
        }
    }

    data class UiModel(
        val isChecked: Boolean
    ) : ValueWrappersListItem {

        override val id: String = "enableAllModulesSwitch"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onStateChanged: (Boolean) -> Unit
        ) = EnableAllModulesSwitchViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_value_wrappers_enable_all_modules_switch, parent, false),
            onStateChanged = onStateChanged
        )
    }
}