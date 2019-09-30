package com.pandulapeter.beagle.views.items.listItem

import com.pandulapeter.beagle.views.items.DrawerItemViewModel
import com.pandulapeter.beagleCore.contracts.BeagleListItemContract

internal data class ListItemViewModel<T : BeagleListItemContract>(
    val listModuleId: String,
    val item: T,
    val onItemSelected: (T) -> Unit
) : DrawerItemViewModel {

    override val id = "${listModuleId}_${item.id}"
    val name = item.name

    fun invokeItemSelectedCallback() = onItemSelected(item)
}