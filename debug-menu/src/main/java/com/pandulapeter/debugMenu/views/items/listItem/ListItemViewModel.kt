package com.pandulapeter.debugMenu.views.items.listItem

import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel
import com.pandulapeter.debugMenuCore.configuration.modules.DebugMenuListItem

internal data class ListItemViewModel<T : DebugMenuListItem>(
    val listModuleId: String,
    val item: T,
    val onItemSelected: (T) -> Unit
) : DrawerItemViewModel {

    override val id = "${listModuleId}_${item.id}"
    val name = item.name

    fun invokeItemSelectedCallback() = onItemSelected(item)
}