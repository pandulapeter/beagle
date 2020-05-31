package com.pandulapeter.beagle.common.contracts

/**
 * All Beagle modules must implement this interface.
 */
interface Module {

    /**
     * Every module must have a unique ID. If a module can be instantiated multiple times, each instance must have a different ID.
     */
    val id: String

    /**
     * A module's UI is represented by one or more instances of [ModuleCell].
     */
    fun createCells(): List<ModuleCell>
}