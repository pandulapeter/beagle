package com.pandulapeter.beagle.views.items.slider

import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Trick

internal data class SliderViewModel(
    val trick: Trick.Slider
) : DrawerItemViewModel {

    override val id = trick.id
    override val shouldUsePayloads = true
}