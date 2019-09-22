package com.pandulapeter.debugMenu.views.items.listItem

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.ItemListModule

internal data class ListItemViewModel(
    val itemListModule: ItemListModule<*>,
    val item: ItemListModule.Item
) : DrawerItem {

    override val id = "${itemListModule.id}_${item.id}"
    val name = item.name
}