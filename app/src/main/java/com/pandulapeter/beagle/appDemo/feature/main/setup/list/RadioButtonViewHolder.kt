package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemSetupRadioButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class RadioButtonViewHolder private constructor(
    binding: ItemSetupRadioButtonBinding,
    onRadioButtonSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemSetupRadioButtonBinding, RadioButtonViewHolder.UiModel>(binding) {

    init {
        binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && isChecked && isChecked != binding.uiModel?.isChecked) {
                binding.uiModel?.let(onRadioButtonSelected)
            }
        }
    }

    data class UiModel(
        @StringRes val titleResourceId: Int,
        val isChecked: Boolean,
        override val id: String = "radioButton_$titleResourceId"
    ) : SetupListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onRadioButtonSelected: (UiModel) -> Unit
        ) = RadioButtonViewHolder(
            binding = ItemSetupRadioButtonBinding.inflate(parent.inflater, parent, false),
            onRadioButtonSelected = onRadioButtonSelected
        )
    }
}