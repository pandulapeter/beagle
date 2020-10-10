package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import java.util.UUID

/**
 * Displays a list of simple items represented by [BeagleListItemContract].
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param items - The list of items that should be displayed.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param onItemSelected - Callback called when the user selects one of the items. The parameter is the selected item. Optional, null by default.
 */
data class ItemListModule<T : BeagleListItemContract>(
    val title: Text,
    val items: List<T>,
    override val isExpandedInitially: Boolean = false,
    override val id: String = UUID.randomUUID().toString(),
    val onItemSelected: ((item: T) -> Unit)? = null
) : ExpandableModule<ItemListModule<T>> {

    override fun getInternalTitle(beagle: BeagleContract) = title

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}