package com.pandulapeter.debugMenuCore.configuration.modules

import java.util.UUID

/**
 * Displays a list of radio buttons. A possible use case could be changing the base URL of the application to simplify testing on different backend environments.
 * The class is generic to a representation of a list item which must implement the [SingleSelectionListModule.Item] interface.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
 * @param title - The text that appears in the header of the module.
 * @param items - The hardcoded list of items implementing the [ListModule.Item] interface.
 * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 * @param initialSelectionId - The ID of the item that is selected when the drawer is opened for the first time.
 * @param onItemSelectionChanged - The callback that will get executed when the selected item is changed.
 */
data class SingleSelectionListModule<T : ListModule.Item>(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    override val isInitiallyExpanded: Boolean = false,
    val items: List<T>,
    val initialSelectionId: String,
    val onItemSelectionChanged: (item: T) -> Unit
) : ExpandableDebugMenuModule {

    fun invokeItemSelectedCallback(id: String) = onItemSelectionChanged(items.first { it.id == id })
}