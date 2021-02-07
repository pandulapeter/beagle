package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemPlaygroundAddModuleBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

class AddModuleViewHolder private constructor(
    binding: ItemPlaygroundAddModuleBinding,
    onAddModuleClicked: () -> Unit
) : BaseViewHolder<ItemPlaygroundAddModuleBinding, AddModuleViewHolder.UiModel>(binding) {

    private val drawableAdd by lazy { itemView.context.tintedDrawable(R.drawable.ic_add, binding.title.textColors.defaultColor) }

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onAddModuleClicked()
            }
        }
    }

    override fun bind(uiModel: UiModel) = binding.title.run {
        super.bind(uiModel)
        setCompoundDrawablesWithIntrinsicBounds(drawableAdd, null, null, null)
    }

    data class UiModel(
        override val id: String = "addModule"
    ) : PlaygroundListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onAddModuleClicked: () -> Unit
        ) = AddModuleViewHolder(
            binding = ItemPlaygroundAddModuleBinding.inflate(parent.inflater, parent, false),
            onAddModuleClicked = onAddModuleClicked
        )
    }
}