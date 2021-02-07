package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.ModuleWrapper
import com.pandulapeter.beagle.appDemo.databinding.ItemPlaygroundModuleBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

@SuppressLint("ClickableViewAccessibility")
class ModuleViewHolder private constructor(
    binding: ItemPlaygroundModuleBinding,
    onDragHandleTouched: (RecyclerView.ViewHolder) -> Unit
) : BaseViewHolder<ItemPlaygroundModuleBinding, ModuleViewHolder.UiModel>(binding) {

    private val drawableDragHandle by lazy { itemView.context.tintedDrawable(R.drawable.ic_drag_handle, binding.title.textColors.defaultColor) }

    init {
        binding.dragHandle.setOnTouchListener { _, event ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && event.action == MotionEvent.ACTION_DOWN) {
                onDragHandleTouched(this)
            }
            false
        }
    }

    override fun bind(uiModel: UiModel) {
        super.bind(uiModel)
        binding.dragHandle.setImageDrawable(drawableDragHandle)
    }

    data class UiModel(
        val moduleWrapper: ModuleWrapper,
        override val id: String = "module_${moduleWrapper.id}"
    ) : PlaygroundListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onDragHandleTouched: (RecyclerView.ViewHolder) -> Unit
        ) = ModuleViewHolder(
            binding = ItemPlaygroundModuleBinding.inflate(parent.inflater, parent, false),
            onDragHandleTouched = onDragHandleTouched
        )
    }
}