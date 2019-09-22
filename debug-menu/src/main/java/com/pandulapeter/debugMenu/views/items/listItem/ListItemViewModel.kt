package com.pandulapeter.debugMenu.views.items.listItem

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule

internal data class ListItemViewModel(
    val itemListModule: ListModule<*>,
    val item: ListModule.Item
) : DrawerItem {

    override val id = "${itemListModule.id}_${item.id}"
    val name = item.name
}