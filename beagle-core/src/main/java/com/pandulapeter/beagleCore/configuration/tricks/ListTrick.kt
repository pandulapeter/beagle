package com.pandulapeter.beagleCore.configuration.tricks

import com.pandulapeter.beagleCore.contracts.BeagleListItemContract
import java.util.UUID

/**
 * Displays an expandable list of custom items and exposes a callback when the user makes a selection. A possible use case could be providing a list of test accounts to make the authentication flow faster.
 * The class is generic to a representation of a list item which must implement the [BeagleListItemContract] interface.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
 * @param title - The text that appears in the header of the module.
 * @param items - The hardcoded list of items implementing the [BeagleListItemContract] interface.
 * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 * @param onItemSelected - The callback that will get executed when an item is selected.
 */
data class ListTrick<T : BeagleListItemContract>(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    override val isInitiallyExpanded: Boolean = false,
    val items: List<T>,
    val onItemSelected: (item: T) -> Unit
) : ExpandableTrick {

    fun invokeItemSelectedCallback(id: String) = onItemSelected(items.first { it.id == id })
}

@Suppress("unused")
typealias ListModule<T> = ListTrick<T>