package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemPlaygroundButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class ButtonViewHolder private constructor(
    binding: ItemPlaygroundButtonBinding,
    onButtonClicked: (UiModel) -> Unit
) : BaseViewHolder<ItemPlaygroundButtonBinding, ButtonViewHolder.UiModel>(binding) {

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.let(onButtonClicked)
            }
        }
    }

    data class UiModel(
        @StringRes val textResourceId: Int,
        override val id: String = "button_$textResourceId"
    ) : PlaygroundListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonClicked: (UiModel) -> Unit
        ) = ButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_playground_button, parent, false),
            onButtonClicked = onButtonClicked
        )
    }
}