package com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemAnalyticsClearButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class ClearButtonViewHolder private constructor(
    binding: ItemAnalyticsClearButtonBinding,
    onClearButtonPressed: () -> Unit
) : BaseViewHolder<ItemAnalyticsClearButtonBinding, ClearButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClearButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "clearButton"
    ) : AnalyticsListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onClearButtonPressed: () -> Unit
        ) = ClearButtonViewHolder(
            binding = ItemAnalyticsClearButtonBinding.inflate(parent.inflater, parent, false),
            onClearButtonPressed = onClearButtonPressed
        )
    }
}