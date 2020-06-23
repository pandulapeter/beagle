package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleWrapper
import com.pandulapeter.beagle.appDemo.databinding.ItemPlaygroundModuleBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.utils.tintedDrawable

class ModuleViewHolder private constructor(
    binding: ItemPlaygroundModuleBinding
) : BaseViewHolder<ItemPlaygroundModuleBinding, ModuleViewHolder.UiModel>(binding) {

    private val drawableDragHandle by lazy { itemView.context.tintedDrawable(R.drawable.ic_drag_handle, binding.title.textColors.defaultColor) }

    override fun bind(uiModel: UiModel) = binding.title.run {
        super.bind(uiModel)
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDragHandle, null)
    }

    data class UiModel(
        val moduleWrapper: ModuleWrapper,
        override val id: String = "module_${moduleWrapper.id}"
    ) : PlaygroundListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = ModuleViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_playground_module, parent, false)
        )
    }
}