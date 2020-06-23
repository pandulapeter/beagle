package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSetupHeaderBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.utils.tintedDrawable

class HeaderViewHolder private constructor(
    binding: ItemSetupHeaderBinding,
    onItemSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemSetupHeaderBinding, HeaderViewHolder.UiModel>(binding) {

    //TODO: Replace with animated vector drawables.
    private val drawableExpand by lazy { itemView.context.tintedDrawable(R.drawable.ic_expand, binding.header.textColors.defaultColor) }
    private val drawableCollapse by lazy { itemView.context.tintedDrawable(R.drawable.ic_collapse, binding.header.textColors.defaultColor) }

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.let(onItemSelected)
            }
        }
    }

    override fun bind(uiModel: UiModel) = binding.header.run {
        super.bind(uiModel)
        setCompoundDrawablesWithIntrinsicBounds(null, null, if (uiModel.isExpanded) drawableCollapse else drawableExpand, null)
    }

    data class UiModel(
        @StringRes val titleResourceId: Int,
        val isExpanded: Boolean,
        override val id: String = "header_$titleResourceId"
    ) : SetupListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (UiModel) -> Unit
        ) = HeaderViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_setup_header, parent, false),
            onItemSelected = onItemSelected
        )
    }
}