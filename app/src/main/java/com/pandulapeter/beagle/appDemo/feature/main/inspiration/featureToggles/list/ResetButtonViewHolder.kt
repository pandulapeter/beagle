package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemFeatureTogglesResetButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class ResetButtonViewHolder private constructor(
    binding: ItemFeatureTogglesResetButtonBinding,
    onButtonPressed: () -> Unit
) : BaseViewHolder<ItemFeatureTogglesResetButtonBinding, ResetButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "resetButton"
    ) : FeatureTogglesListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: () -> Unit
        ) = ResetButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_feature_toggles_reset_button, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}