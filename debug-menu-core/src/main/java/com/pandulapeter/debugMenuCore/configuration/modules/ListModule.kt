package com.pandulapeter.debugMenuCore.configuration.modules

import java.util.UUID

/**
 * Displays an expandable list of custom items and exposes a callback when the user makes a selection.
 * Possible use cases could be providing a list of test accounts to make the login process faster or allowing the user to switch between backend environments.
 * The class is generic to a representation of a list item which must implement the [ListModule.Item] interface.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
 * @param title - The text that appears in the header of the module.
 * @param items - The hardcoded list of items.
 * @param onItemSelected - The callback that will be executed when an item is selected.
 */
//TODO: Create a more generic implementation that also supports selection states (normal, checkbox, radio button).
data class ListModule<T : ListModule.Item>(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    val items: List<T>,
    val onItemSelected: (id: String) -> Unit
) : ExpandableDebugMenuModule {

    /**
     * Represents a single item from the list. The ID must be a unique to the list (it's used in the callback) while the name is the text displayed on the UI.
     */
    interface Item {
        val id: String
        val name: String
    }
}