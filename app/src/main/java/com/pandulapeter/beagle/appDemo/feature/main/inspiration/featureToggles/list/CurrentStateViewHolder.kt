package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemFeatureTogglesCurrentStateBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class CurrentStateViewHolder private constructor(
    binding: ItemFeatureTogglesCurrentStateBinding
) : BaseViewHolder<ItemFeatureTogglesCurrentStateBinding, CurrentStateViewHolder.UiModel>(binding) {

    data class UiModel(
        private val toggle1: Boolean,
        private val toggle2: Boolean,
        private val toggle3: Boolean,
        private val toggle4: Boolean,
        private val multipleSelectionOptions: List<String>,
        private val singleSelectionOption: String?
    ) : FeatureTogglesListItem {

        override val id = "currentState"
        val t1 = if (toggle1) "ON" else "OFF"
        val t2 = if (toggle2) "ON" else "OFF"
        val t3 = if (toggle3) "ON" else "OFF"
        val t4 = if (toggle4) "ON" else "OFF"
        val t5 = if (multipleSelectionOptions.isEmpty()) "-" else multipleSelectionOptions.sorted().joinToString()
        val t6 = singleSelectionOption ?: "-"
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = CurrentStateViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_feature_toggles_current_state, parent, false)
        )
    }
}