package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorRadioButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

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
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_network_request_interceptor_radio_button, parent, false),
            onRadioButtonSelected = onRadioButtonSelected
        )
    }
}