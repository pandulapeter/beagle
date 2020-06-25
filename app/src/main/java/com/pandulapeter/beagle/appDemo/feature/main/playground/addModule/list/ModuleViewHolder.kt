package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleWrapper
import com.pandulapeter.beagle.appDemo.databinding.ItemAddModuleModuleBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class ModuleViewHolder private constructor(
    binding: ItemAddModuleModuleBinding,
    onModuleSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemAddModuleModuleBinding, ModuleViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.let(onModuleSelected)
            }
        }
    }

    data class UiModel(
        val moduleWrapper: ModuleWrapper,
        val isEnabled: Boolean,
        override val id: String = "module_${moduleWrapper.titleResourceId}"
    ) : AddModuleListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onModuleSelected: (UiModel) -> Unit
        ) = ModuleViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_add_module_module, parent, false),
            onModuleSelected = onModuleSelected
        )
    }
}