package com.pandulapeter.beagle.appDemo.data

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

data class SampleListItem(
    override val title: String
) : BeagleListItemContract {

    override val id = title
}