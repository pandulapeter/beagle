package com.pandulapeter.debugMenu.views.items.authenticationHelperItem

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.ItemListModule

internal data class AuthenticationHelperItemViewModel(
    val itemListModule: ItemListModule<*>,
    val item: ItemListModule.Item
) : DrawerItem {

    override val id = "${itemListModule.id}_${item.id}"
    val name = item.name
}