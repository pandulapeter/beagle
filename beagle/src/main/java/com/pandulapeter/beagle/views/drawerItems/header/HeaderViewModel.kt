package com.pandulapeter.beagle.views.drawerItems.header

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Trick

internal data class HeaderViewModel(
    private val headerTrick: Trick.Header
) : DrawerItemViewModel {

    override val id = ID
    val title = headerTrick.title
    val subtitle = headerTrick.subtitle
    val text = headerTrick.text

    companion object {
        const val ID = "header"
    }
}