package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Represents a single item from the list. The ID must be a unique to the list while the name is the text displayed on the UI.
 */
interface DebugMenuListItem {
    val id: String get() = name
    val name: String
}