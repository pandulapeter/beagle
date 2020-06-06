package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import java.util.UUID

/**
 * Displays a list of simple items represented by [BeagleListItemContract] instances.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param color - The resolved color for the title and the elements. Optional, color from theme is used by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param items - The list of items that should be displayed.
 * @param onItemSelected - Callback called when the user selects one of the items. The parameter is the ID of the selected item. Optional, null by default.
 */
data class ItemListModule(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    @ColorInt override val color: Int? = null,
    override val isExpandedInitially: Boolean = false,
    val items: List<BeagleListItemContract>,
    val onItemSelected: ((id: String) -> Unit)? = null
) : ExpandableModule<ItemListModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}