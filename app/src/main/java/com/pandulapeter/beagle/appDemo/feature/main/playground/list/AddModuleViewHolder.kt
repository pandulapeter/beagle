package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemPlaygroundAddModuleBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class AddModuleViewHolder private constructor(
    binding: ItemPlaygroundAddModuleBinding,
    onAddModuleClicked: () -> Unit
) : BaseViewHolder<ItemPlaygroundAddModuleBinding, AddModuleViewHolder.UiModel>(binding) {

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onAddModuleClicked()
            }
        }
    }

    data class UiModel(
        override val id: String = "addModule"
    ) : PlaygroundListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onAddModuleClicked: () -> Unit
        ) = AddModuleViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_playground_add_module, parent, false),
            onAddModuleClicked = onAddModuleClicked
        )
    }
}