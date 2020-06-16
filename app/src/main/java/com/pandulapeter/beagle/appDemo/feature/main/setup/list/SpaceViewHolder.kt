package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSetupSpaceBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import java.util.UUID

class SpaceViewHolder private constructor(
    binding: ItemSetupSpaceBinding
) : BaseViewHolder<ItemSetupSpaceBinding, SpaceViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = "space_${UUID.randomUUID()}"
    ) : SetupListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = SpaceViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_setup_space, parent, false)
        )
    }
}