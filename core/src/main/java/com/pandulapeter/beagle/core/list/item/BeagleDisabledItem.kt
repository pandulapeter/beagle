package com.pandulapeter.beagle.core.list.item

import com.pandulapeter.beagle.common.contracts.BeagleModuleItem

data class BeagleDisabledItem(
    override val id: String = "disabled"
) : BeagleModuleItem