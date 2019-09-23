package com.pandulapeter.debugMenu.views.items.header

import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule

internal data class HeaderViewModel(
    private val headerModule: HeaderModule
) : DrawerItemViewModel {

    override val id = ID
    val title = headerModule.title
    val subtitle = headerModule.subtitle
    val text = headerModule.text

    companion object {
        const val ID = "header"
    }
}