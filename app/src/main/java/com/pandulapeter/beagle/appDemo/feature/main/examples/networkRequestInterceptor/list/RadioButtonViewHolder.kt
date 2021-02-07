package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorRadioButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class RadioButtonViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorRadioButtonBinding,
    onRadioButtonSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemNetworkRequestInterceptorRadioButtonBinding, RadioButtonViewHolder.UiModel>(binding) {

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
    ) : NetworkRequestInterceptorListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onRadioButtonSelected: (UiModel) -> Unit
        ) = RadioButtonViewHolder(
            binding = ItemNetworkRequestInterceptorRadioButtonBinding.inflate(parent.inflater, parent, false),
            onRadioButtonSelected = onRadioButtonSelected
        )
    }
}