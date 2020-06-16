package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSetupRadioButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class RadioButtonViewHolder private constructor(
    binding: ItemSetupRadioButtonBinding,
    onRadioButtonSelected: (Int) -> Unit
) : BaseViewHolder<ItemSetupRadioButtonBinding, RadioButtonViewHolder.UiModel>(binding) {

    init {
        binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION && isChecked && isChecked != binding.uiModel?.isChecked) {
                    onRadioButtonSelected(adapterPosition)
                }
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
            onRadioButtonSelected: (Int) -> Unit
        ) = RadioButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_setup_radio_button, parent, false),
            onRadioButtonSelected = onRadioButtonSelected
        )
    }
}