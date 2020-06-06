package com.pandulapeter.beagle.common.contracts

/**
 * Models used for representing items in the ItemListModule, SingleSelectionListModule or MultipleSelectionListModule must implement this interface.
 */
interface BeagleListItemContract {

    /**
     * A unique identifier for the list item.
     */
    val id: String

    /**
     * The text that should appear on the UI.
     */
    val text: String
}