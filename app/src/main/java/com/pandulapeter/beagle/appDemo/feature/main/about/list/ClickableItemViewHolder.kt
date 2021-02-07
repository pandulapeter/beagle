package com.pandulapeter.beagle.appDemo.feature.main.about.list

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemAboutClickableItemBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

class ClickableItemViewHolder private constructor(
    binding: ItemAboutClickableItemBinding,
    onItemClicked: (UiModel) -> Unit
) : BaseViewHolder<ItemAboutClickableItemBinding, ClickableItemViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.let(onItemClicked)
            }
        }
    }

    override fun bind(uiModel: UiModel) = binding.title.run {
        super.bind(uiModel)
        setCompoundDrawablesWithIntrinsicBounds(itemView.context.tintedDrawable(uiModel.drawableResourceId, binding.title.textColors.defaultColor), null, null, null)
    }

    data class UiModel(
        @StringRes val textResourceId: Int,
        @DrawableRes val drawableResourceId: Int,
        override val id: String = "clickableItem_$textResourceId"
    ) : AboutListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (UiModel) -> Unit
        ) = ClickableItemViewHolder(
            binding = ItemAboutClickableItemBinding.inflate(parent.inflater, parent, false),
            onItemClicked = onItemClicked
        )
    }
}