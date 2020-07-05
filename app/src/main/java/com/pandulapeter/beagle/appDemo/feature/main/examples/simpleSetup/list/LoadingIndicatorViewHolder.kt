package com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSimpleSetupLoadingIndicatorBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import java.util.UUID

class LoadingIndicatorViewHolder private constructor(
    binding: ItemSimpleSetupLoadingIndicatorBinding
) : BaseViewHolder<ItemSimpleSetupLoadingIndicatorBinding, LoadingIndicatorViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = UUID.randomUUID().toString()
    ) : SimpleSetupListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = LoadingIndicatorViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_simple_setup_loading_indicator, parent, false)
        )
    }
}