package com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemAuthenticationButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class ButtonViewHolder private constructor(
    binding: ItemAuthenticationButtonBinding,
    onButtonPressed: () -> Unit
) : BaseViewHolder<ItemAuthenticationButtonBinding, ButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "button"
    ) : AuthenticationListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: () -> Unit
        ) = ButtonViewHolder(
            binding = ItemAuthenticationButtonBinding.inflate(parent.inflater, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}