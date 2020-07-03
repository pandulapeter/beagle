package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSectionHeaderBinding
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list.FeatureTogglesListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.utils.tintedDrawable

class SectionHeaderViewHolder private constructor(
    binding: ItemSectionHeaderBinding,
    onItemSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemSectionHeaderBinding, SectionHeaderViewHolder.UiModel>(binding) {

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
        override val id: String = "sectionHeader_$titleResourceId"
    ) : ListItem, SetupListItem, BasicSetupListItem, FeatureTogglesListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (UiModel) -> Unit
        ) = SectionHeaderViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_section_header, parent, false),
            onItemSelected = onItemSelected
        )
    }
}