package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemValueWrappersResetButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

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
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_value_wrappers_reset_button, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}