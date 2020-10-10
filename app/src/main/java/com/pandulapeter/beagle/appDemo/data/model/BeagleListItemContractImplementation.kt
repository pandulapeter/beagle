package com.pandulapeter.beagle.appDemo.data.model

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract

data class BeagleListItemContractImplementation(
    override val title: Text
) : BeagleListItemContract