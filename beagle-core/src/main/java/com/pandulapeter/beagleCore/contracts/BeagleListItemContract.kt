package com.pandulapeter.beagleCore.contracts

/**
 * Represents a single item from a list. The ID must be a unique to the list while the name is the text displayed on the UI.
 */
@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
interface BeagleListItemContract {

    val id: String get() = name
    val name: String
}