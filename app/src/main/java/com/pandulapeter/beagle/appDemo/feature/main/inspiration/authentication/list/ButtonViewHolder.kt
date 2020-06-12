package com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemAuthenticationButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class ButtonViewHolder private constructor(
    binding: ItemAuthenticationButtonBinding,
    onButtonClicked: () -> Unit
) : BaseViewHolder<ItemAuthenticationButtonBinding, ButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onButtonClicked()
            }
        }
    }

    data class UiModel(
        override val id: String = "button"
    ) : AuthenticationListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonClicked: () -> Unit
        ) = ButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_authentication_button, parent, false),
            onButtonClicked = onButtonClicked
        )
    }
}