package com.pandulapeter.beagle.appDemo.feature.main.inspiration.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemInspirationSectionBinding
import com.pandulapeter.beagle.appDemo.utils.ViewHolder

class SectionViewHolder private constructor(
    binding: ItemInspirationSectionBinding
) : ViewHolder<ItemInspirationSectionBinding, SectionViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = "section"
    ) : HomeListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = SectionViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_inspiration_section, parent, false)
        )
    }
}