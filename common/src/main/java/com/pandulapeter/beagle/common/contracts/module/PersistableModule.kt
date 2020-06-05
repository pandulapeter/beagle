package com.pandulapeter.beagle.common.contracts.module

import com.pandulapeter.beagle.common.contracts.BeagleContract

/**
 * Modules that wrap primitives that can be persisted in SharedPreferences must implement this interface.
 * The save-load functionality is optional and will only work if the ID of the module is constant across app sessions.
 */
interface PersistableModule<T, M : Module<M>> : Module<M> {

    /**
     * The initial value.
     * If [shouldBePersisted] is true, the value coming from the local storage will override this field so it will only be used the first time the app is launched.
     */
    val initialValue: T

    /**
     * Can be used to enable or disable persisting the value on the local storage.
     */
    //TODO: Create a Lint warning to enforce overriding the module ID if this property is true.
    val shouldBePersisted: Boolean

    /**
     * Callback triggered when the user modifies the value.
     */
    val onValueChanged: (newValue: T) -> Unit

    /**
     * For every custom module a custom [Delegate] needs to be registered. Built-in modules use a different mechanism to achieve an empty implementation in the noop variant.
     */
    override fun createModuleDelegate(): Delegate<T, M>

    /**
     * Can be used to query the current value at any time.
     *
     * @param beagle - This should simply be the Beagle singleton.
     *
     * @return - The current value or null if the module delegate was not found (this will be the case in the noop variant).
     */
    @Suppress("UNCHECKED_CAST")
    fun getCurrentValue(beagle: BeagleContract): T? = getCurrentValue(beagle.findModuleDelegate((this as M)::class))

    @Suppress("UNCHECKED_CAST") //TODO: Merge with function above
    private fun getCurrentValue(moduleDelegate: Module.Delegate<M>?): T? = (moduleDelegate as? Delegate<T, M>?)?.getCurrentValue(this as M)

    /**
     * Can be used to update the current value at any time. Changes should also be reflected on the UI of the debug menu.
     *
     * @param beagle - This should simply be the Beagle singleton.
     * @param newValue - The new value.
     */
    @Suppress("UNCHECKED_CAST")
    fun setCurrentValue(beagle: BeagleContract, newValue: T) = setCurrentValue(beagle.findModuleDelegate((this as M)::class), newValue)

    @Suppress("UNCHECKED_CAST") //TODO: Merge with function above
    private fun setCurrentValue(moduleDelegate: Module.Delegate<M>?, newValue: T) {
        (moduleDelegate as? Delegate<T, M>?)?.setCurrentValue(this as M, newValue)
    }

    /**
     * All [PersistableModule] implementations must have their corresponding delegate that contains the implementation details.
     */
    abstract class Delegate<T, M : Module<M>> : Module.Delegate<M>() {

        /**
         * Returns the current value.
         */
        abstract fun getCurrentValue(module: M): T

        /**
         * Updates the current value.
         */
        abstract fun setCurrentValue(module: M, newValue: T)
    }
}