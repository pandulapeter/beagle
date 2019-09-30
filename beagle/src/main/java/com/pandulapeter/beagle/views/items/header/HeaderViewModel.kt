package com.pandulapeter.beagle.views.items.header

import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.tricks.HeaderTrick

internal data class HeaderViewModel(
    private val headerTrick: HeaderTrick
) : DrawerItemViewModel {

    override val id = ID
    val title = headerTrick.title
    val subtitle = headerTrick.subtitle
    val text = headerTrick.text

    companion object {
        const val ID = "header"
    }
}