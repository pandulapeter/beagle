package com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.databinding.ItemSimpleSetupLoadingIndicatorBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater
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
            binding = ItemSimpleSetupLoadingIndicatorBinding.inflate(parent.inflater, parent, false)
        )
    }
}