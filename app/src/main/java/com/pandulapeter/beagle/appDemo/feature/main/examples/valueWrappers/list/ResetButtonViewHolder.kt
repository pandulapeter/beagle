package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemValueWrappersResetButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class ResetButtonViewHolder private constructor(
    binding: ItemValueWrappersResetButtonBinding,
    onButtonPressed: () -> Unit
) : BaseViewHolder<ItemValueWrappersResetButtonBinding, ResetButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "resetButton"
    ) : ValueWrappersListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: () -> Unit
        ) = ResetButtonViewHolder(
            binding = ItemValueWrappersResetButtonBinding.inflate(parent.inflater, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}