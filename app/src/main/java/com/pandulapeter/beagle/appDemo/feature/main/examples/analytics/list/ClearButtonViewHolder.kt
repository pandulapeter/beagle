package com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemAnalyticsClearButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

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
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_analytics_clear_button, parent, false),
            onClearButtonPressed = onClearButtonPressed
        )
    }
}