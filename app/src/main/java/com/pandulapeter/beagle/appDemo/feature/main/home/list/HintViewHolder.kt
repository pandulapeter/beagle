package com.pandulapeter.beagle.appDemo.feature.main.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemHomeHintBinding
import com.pandulapeter.beagle.appDemo.utils.ViewHolder

class HintViewHolder private constructor(binding: ItemHomeHintBinding) : ViewHolder<ItemHomeHintBinding, HintViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = "hint"
    ) : HomeListItem

    companion object {
        fun create(parent: ViewGroup) = HintViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_home_hint, parent, false))
    }
}