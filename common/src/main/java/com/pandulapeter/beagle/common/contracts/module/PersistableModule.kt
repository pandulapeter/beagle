package com.pandulapeter.beagle.common.contracts.module

import com.pandulapeter.beagle.common.contracts.BeagleContract

/**
 * Modules that wrap primitives that can be persisted in SharedPreferences must implement this interface.
 * The save-load functionality is optional and will only work if the ID of the module is constant across app sessions.
 *
 * @param T - The type of data to persist.
 * @param M - The type of the module.
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
     * Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step.
     */
    val shouldRequireConfirmation: Boolean

    /**
     * Callback triggered when the user modifies the value.
     */
    val onValueChanged: (newValue: T) -> Unit

    /**
     * For every custom module a custom [Delegate] needs to be registered. Built-in modules use a different mechanism to achieve an empty implementation in the noop variant.
     */
    override fun createModuleDelegate(): Delegate<T, M>

    //TODO: Reduce code duplication in the functions below.
    /**
     * Returns whether or not the module has any pending changes at the moment.
     * Not designed to be overridden.
     *
     * @param beagle - This should simply be the Beagle singleton.
     *
     * @return True if there are pending changes for the module, false otherwise.
     */
    @Suppress("UNCHECKED_CAST")
    fun hasPendingChanges(beagle: BeagleContract): Boolean = (beagle.delegateFor((this as M)::class) as? Delegate<T, M>?)?.hasPendingChanges(this as M) == true

    /**
     * Performs the pending change event after the user confirms it. The [hasPendingChanges] function is expected to return false after this call.
     * Not designed to be overridden.
     *
     * @param beagle - This should simply be the Beagle singleton.
     */
    @Suppress("UNCHECKED_CAST")
    fun applyPendingChanges(beagle: BeagleContract) = (beagle.delegateFor((this as M)::class) as? Delegate<T, M>?)?.applyPendingChanges(this as M)

    /**
     * Reverts the pending change event. The [hasPendingChanges] function is expected to return false after this call.
     * Not designed to be overridden.
     *
     * @param beagle - This should simply be the Beagle singleton.
     */
    @Suppress("UNCHECKED_CAST")
    fun resetPendingChanges(beagle: BeagleContract) = (beagle.delegateFor((this as M)::class) as? Delegate<T, M>?)?.resetPendingChanges(this as M)

    /**
     * Can be used to query the current value at any time.
     * Not designed to be overridden.
     *
     * @param beagle - This should simply be the Beagle singleton.
     *
     * @return - The current value or null if the module delegate was not found (this will be the case in the noop variant).
     */
    @Suppress("UNCHECKED_CAST")
    fun getCurrentValue(beagle: BeagleContract): T? = (beagle.delegateFor((this as M)::class) as? Delegate<T, M>?)?.getCurrentValue(this as M)

    /**
     * Can be used to update the current value at any time. Changes should also be reflected on the UI of the debug menu.
     * Not designed to be overridden.
     *
     * @param beagle - This should simply be the Beagle singleton.
     * @param newValue - The new value.
     */
    @Suppress("UNCHECKED_CAST")
    fun setCurrentValue(beagle: BeagleContract, newValue: T) = (beagle.delegateFor((this as M)::class) as? Delegate<T, M>?)?.setCurrentValue(this as M, newValue)

    /**
     * All [PersistableModule] implementations must have their corresponding delegate that contains the implementation details.
     *
     * @param T - The type of data to persist.
     * @param M - The type of the module.
     */
    interface Delegate<T, M : Module<M>> : Module.Delegate<M> {

        /**
         * Returns whether or not the module has any pending changes at the moment.
         *
         * @param module - The module to check.
         *
         * @return True if there are pending changes for the module, false otherwise.
         */
        fun hasPendingChanges(module: M): Boolean

        /**
         * Performs the pending change event after the user confirms it. The [hasPendingChanges] function for the same module is expected to return false after this call.
         *
         * @param module - The module for which the changes should be applied.
         */
        fun applyPendingChanges(module: M)

        /**
         * Reverts the pending change event. The [hasPendingChanges] function for the same module is expected to return false after this call.
         *
         * @param module - The module for which the changes should be reset.
         */
        fun resetPendingChanges(module: M)

        /**
         * Returns the current value.
         */
        fun getCurrentValue(module: M): T

        /**
         * Updates the current value.
         */
        fun setCurrentValue(module: M, newValue: T)
    }
}