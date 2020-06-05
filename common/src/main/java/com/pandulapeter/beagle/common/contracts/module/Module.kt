package com.pandulapeter.beagle.common.contracts.module

/**
 * All Beagle modules must implement this interface. Modules are lightweight classes containing the parameters needed from the consumer.
 */
interface Module<M : Module<M>> {

    /**
     * Every module must have a unique ID. If a module can be instantiated multiple times, each instance must have a different ID.
     */
    val id: String

    /**
     * If you write a custom module, its custom [ModuleDelegate] needs to be registered. Built-in modules use a different mechanism to achieve an empty implementation in the noop variant.
     */
    fun createModuleDelegate(): ModuleDelegate<M>

    /**
     * Derived classes are encouraged to be data classes.
     */
    override fun equals(other: Any?): Boolean

    /**
     * Derived classes are encouraged to be data classes.
     */
    override fun hashCode(): Int
}