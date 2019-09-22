package com.pandulapeter.debugMenu.views.items.authenticationHelperItem

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.AuthenticationHelperModule

internal data class AuthenticationHelperItemViewModel(
    val authenticationHelperModule: AuthenticationHelperModule<*>,
    val item: AuthenticationHelperModule.Item
) : DrawerItem {

    override val id = "${authenticationHelperModule.id}_${item.id}"
    val name = item.name
}