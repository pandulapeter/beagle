package com.pandulapeter.beagle.common.contracts

/**
 * All Beagle modules must implement this interface.
 */
interface Module {

    val id: String

    fun createCells(): List<Cell>

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}