package com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemFeatureFlagsResetButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class ResetButtonViewHolder private constructor(
    binding: ItemFeatureFlagsResetButtonBinding,
    onButtonPressed: () -> Unit
) : BaseViewHolder<ItemFeatureFlagsResetButtonBinding, ResetButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "resetButton"
    ) : FeatureFlagsListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: () -> Unit
        ) = ResetButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_feature_flags_reset_button, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}