package com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemAnalyticsCheckBoxBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class CheckBoxViewHolder private constructor(
    binding: ItemAnalyticsCheckBoxBinding,
    onStateChanged: (Int, Boolean) -> Unit
) : BaseViewHolder<ItemAnalyticsCheckBoxBinding, CheckBoxViewHolder.UiModel>(binding) {

    init {
        binding.bulkApplySwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.uiModel?.text?.let { text ->
                if (bindingAdapterPosition != RecyclerView.NO_POSITION && isChecked != binding.uiModel?.isChecked) {
                    onStateChanged(text, isChecked)
                }
            }
        }
    }

    data class UiModel(
        @StringRes val text: Int,
        val isChecked: Boolean
    ) : AnalyticsListItem {

        override val id: String = "switch_$text"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onStateChanged: (Int, Boolean) -> Unit
        ) = CheckBoxViewHolder(
            binding = ItemAnalyticsCheckBoxBinding.inflate(parent.inflater, parent, false),
            onStateChanged = onStateChanged
        )
    }
}