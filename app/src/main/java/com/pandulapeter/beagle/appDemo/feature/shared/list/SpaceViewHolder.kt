package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.databinding.ItemSpaceBinding
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list.AuthenticationListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list.ValueWrappersListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.utils.extensions.inflater
import java.util.UUID

class SpaceViewHolder private constructor(
    binding: ItemSpaceBinding
) : BaseViewHolder<ItemSpaceBinding, SpaceViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = "space_${UUID.randomUUID()}"
    ) : ListItem, SetupListItem, ExamplesListItem, SimpleSetupListItem, ValueWrappersListItem, AuthenticationListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = SpaceViewHolder(
            binding = ItemSpaceBinding.inflate(parent.inflater, parent, false)
        )
    }
}