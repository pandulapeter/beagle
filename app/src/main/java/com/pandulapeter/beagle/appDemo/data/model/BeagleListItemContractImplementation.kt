package com.pandulapeter.beagle.appDemo.data.model

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

data class BeagleListItemContractImplementation(
    override val title: String
) : BeagleListItemContract {

    override val id = title
}