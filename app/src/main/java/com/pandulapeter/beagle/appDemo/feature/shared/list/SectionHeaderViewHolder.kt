package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSectionHeaderBinding
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureToggles.list.FeatureTogglesListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.utils.animatedDrawable
import com.pandulapeter.beagle.appDemo.utils.tintedDrawable

class SectionHeaderViewHolder private constructor(
    binding: ItemSectionHeaderBinding,
    onItemSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemSectionHeaderBinding, SectionHeaderViewHolder.UiModel>(binding) {

    private var isExpanded: Boolean? = null

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.let(onItemSelected)
            }
        }
    }

    override fun bind(uiModel: UiModel) = binding.header.run {
        super.bind(uiModel)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uiModel.isExpanded != isExpanded) {
            isExpanded = uiModel.isExpanded
            setCompoundDrawablesWithIntrinsicBounds(null, null, getAnimatedDrawable(uiModel.isExpanded), null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(uiModel.isExpanded), null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun TextView.getAnimatedDrawable(isExpanded: Boolean) = context.animatedDrawable(if (isExpanded) R.drawable.avd_expand else R.drawable.avd_collapse).apply {
        setTintList(textColors)
        start()
    }

    private fun TextView.getDrawable(isExpanded: Boolean) = context.tintedDrawable(if (isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand, textColors.defaultColor)

    data class UiModel(
        @StringRes val titleResourceId: Int,
        val isExpanded: Boolean,
        override val id: String = "sectionHeader_$titleResourceId"
    ) : ListItem, SetupListItem, SimpleSetupListItem, FeatureTogglesListItem

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