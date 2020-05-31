package com.pandulapeter.beagle.common.contracts

/**
 * UI models representing a single element to be displayed in the debug menu should implement this interface.
 */
interface Cell {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}