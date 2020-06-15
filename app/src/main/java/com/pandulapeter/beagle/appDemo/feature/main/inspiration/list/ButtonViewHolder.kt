package com.pandulapeter.beagle.appDemo.feature.main.inspiration.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemInspirationButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.utils.drawable

class ButtonViewHolder private constructor(
    binding: ItemInspirationButtonBinding,
    onButtonClicked: () -> Unit
) : BaseViewHolder<ItemInspirationButtonBinding, ButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onButtonClicked()
            }
        }
        binding.button.setCompoundDrawablesWithIntrinsicBounds(null, null, binding.button.context.drawable(R.drawable.ic_github), null)
    }

    data class UiModel(
        override val id: String = "button"
    ) : InspirationListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonClicked: () -> Unit
        ) = ButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_inspiration_button, parent, false),
            onButtonClicked = onButtonClicked
        )
    }
}