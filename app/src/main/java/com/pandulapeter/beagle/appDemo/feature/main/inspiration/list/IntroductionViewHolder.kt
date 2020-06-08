package com.pandulapeter.beagle.appDemo.feature.main.inspiration.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemInspirationIntroductionBinding
import com.pandulapeter.beagle.appDemo.utils.ViewHolder

class IntroductionViewHolder private constructor(
    binding: ItemInspirationIntroductionBinding
) : ViewHolder<ItemInspirationIntroductionBinding, IntroductionViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = "hint"
    ) : HomeListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = IntroductionViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_inspiration_introduction, parent, false)
        )
    }
}