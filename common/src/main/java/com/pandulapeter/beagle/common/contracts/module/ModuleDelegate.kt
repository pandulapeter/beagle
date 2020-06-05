package com.pandulapeter.beagle.common.contracts.module

import androidx.annotation.RestrictTo

/**
 * All [Module] implementations must have their corresponding delegate that contains the implementation details.
 */
interface ModuleDelegate<M : Module<M>> {

    /**
     * A module's UI is represented by one or more instances of [Cell]. These can also be different subtypes.
     * This function is called every time the UI should be refreshed. You can manually trigger such refresh by calling [Beagle.updateCells()]
     */
    fun createCells(module: M): List<Cell<*>>

    /**
     * For internal use only.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Suppress("UNCHECKED_CAST")
    fun forceCreateCells(module: Module<*>) = createCells(module as M)
}