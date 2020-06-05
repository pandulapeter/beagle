package com.pandulapeter.beagle.common.contracts.module

/**
 * All [PersistableModule] implementations must have their corresponding delegate that contains the implementation details.
 */
interface PersistableModuleDelegate<T, M : Module<M>> : ModuleDelegate<M> {

    /**
     * Returns the current value.
     */
    fun getCurrentValue(module: M): T

    /**
     * Updates the current value.
     */
    fun setCurrentValue(module: M, newValue: T)
}