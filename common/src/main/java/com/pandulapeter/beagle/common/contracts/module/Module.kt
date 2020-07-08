package com.pandulapeter.beagle.common.contracts.module

import androidx.annotation.RestrictTo

/**
 * All Beagle modules must implement this interface. Modules are lightweight classes containing the parameters needed from the consumer.
 *
 * @param M - The type of the module.
 */
interface Module<M : Module<M>> {

    /**
     * Every module must have a unique ID. If a module can be instantiated multiple times, each instance must have a different ID.
     */
    val id: String

    /**
     * For every custom module a custom [Delegate] needs to be registered. Built-in modules use a different mechanism to achieve an empty implementation in the noop variant.
     */
    fun createModuleDelegate(): Delegate<M>

    /**
     * Derived classes are encouraged to be data classes.
     */
    override fun equals(other: Any?): Boolean

    /**
     * Derived classes are encouraged to be data classes.
     */
    override fun hashCode(): Int

    /**
     * All [Module] implementations must have their corresponding delegate that contains the implementation details.
     *
     * @param M - The type of the module.
     */
    interface Delegate<M : Module<M>> {

        /**
         * A module's UI is represented by one or more instances of [Cell]. These can also be different subtypes.
         * This function is called every time the UI should be refreshed. You can manually trigger such refresh by calling [Beagle.refresh()]
         */
        fun createCells(module: M): List<Cell<*>>

        /**
         * For internal use only.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        @Suppress("UNCHECKED_CAST")
        fun forceCreateCells(module: Module<*>) = createCells(module as M)
    }
}