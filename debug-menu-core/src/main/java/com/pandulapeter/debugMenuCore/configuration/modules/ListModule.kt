package com.pandulapeter.debugMenuCore.configuration.modules

import java.util.UUID

/**
 * Displays an expandable list of custom items and exposes a callback when the user makes a selection.
 * Possible use cases could be providing a list of test accounts to make the login process faster or allowing the user to switch between backend environments.
 * The class is generic to a representation of a list item which must implement the [ListModule.Item] interface.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
 * @param title - The text that appears in the header of the module.
 * @param items - The hardcoded list of items implementing the [ListModule.Item] interface.
 * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 * @param onItemSelected - The callback that will get executed when an item is selected.
 */
data class ListModule<T : ListModule.Item>(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    override val isInitiallyExpanded: Boolean = false,
    val items: List<T>,
    val onItemSelected: (item: T) -> Unit
) : ExpandableDebugMenuModule {

    fun invokeItemSelectedCallback(id: String) = onItemSelected(items.first { it.id == id })

    /**
     * Represents a single item from the list. The ID must be a unique to the list while the name is the text displayed on the UI.
     */
    interface Item {
        val id: String get() = name
        val name: String
    }
}