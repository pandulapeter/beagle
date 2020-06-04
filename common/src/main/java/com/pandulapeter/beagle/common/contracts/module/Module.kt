package com.pandulapeter.beagle.common.contracts.module

/**
 * All Beagle modules must implement this interface.
 */
interface Module {

    /**
     * Every module must have a unique ID. If a module can be instantiated multiple times, each instance must have a different ID.
     */
    val id: String

    /**
     * A module's UI is represented by one or more instances of [Cell]. These can also be different subtypes.
     * This function is called every time the UI should be refreshed. You can manually trigger such refresh by calling [Beagle.updateCells()]
     */
    fun createCells(): List<Cell<*>>
}