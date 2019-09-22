package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays an expandable list of custom items and exposes a callback when the user makes a selection.
 * Possible use cases could be providing a list of test accounts to make the login process faster or allowing the user to switch between backend environments.
 * The class is generic to a representation of a list item which must implement the [ItemListModule.Item] interface.
 *
 * @param title - The text that appears in the header of the module.
 * @param items - The hardcoded list of items.
 * @param onItemSelected - The callback that will be executed when an item is selected.
 */
//TODO: Create a more generic implementation that also supports selection states.
data class ItemListModule<T : ItemListModule.Item>(
    override val id: String,
    override val title: String,
    val items: List<T>,
    val onItemSelected: (id: String) -> Unit
) : ExpandableDebugMenuModule {

    /**
     * Represents a single item from the list (account).
     */
    interface Item {
        val id: String
        val name: String
    }
}