package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemBasicSetupLoadingIndicatorBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import java.util.UUID

class LoadingIndicatorViewHolder private constructor(
    binding: ItemBasicSetupLoadingIndicatorBinding
) : BaseViewHolder<ItemBasicSetupLoadingIndicatorBinding, LoadingIndicatorViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = UUID.randomUUID().toString()
    ) : BasicSetupListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = LoadingIndicatorViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_basic_setup_loading_indicator, parent, false)
        )
    }
}