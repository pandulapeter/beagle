package com.pandulapeter.beagle.views.drawerItems.slider

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Trick

internal data class SliderViewModel(
    val trick: Trick.Slider
) : DrawerItemViewModel {

    override val id = trick.id
}