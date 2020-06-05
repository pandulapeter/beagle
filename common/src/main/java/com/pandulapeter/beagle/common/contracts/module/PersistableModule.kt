package com.pandulapeter.beagle.common.contracts.module

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
     * Can be used to query the current value at any time.
     *
     * @param moduleDelegate - This parameter should be resolved by Beagle.findModuleDelegate(TheSpecificModule::class).
     *
     * @return - The current value or null if the module delegate was not found (this will be the case in the noop variant).
     */
    @Suppress("UNCHECKED_CAST")
    fun getCurrentValue(moduleDelegate: ModuleDelegate<M>?): T? = (moduleDelegate as? PersistableModuleDelegate<T, M>?)?.getCurrentValue(this as M)

    /**
     * Can be used to update the current value at any time. Changes should also be reflected on the UI of the debug menu.
     *
     * @param moduleDelegate - This parameter should be resolved by Beagle.findModuleDelegate(TheSpecificModule::class).
     * @param newValue - The new value.
     */
    @Suppress("UNCHECKED_CAST")
    fun setCurrentValue(moduleDelegate: ModuleDelegate<M>?, newValue: T) {
        (moduleDelegate as? PersistableModuleDelegate<T, M>?)?.setCurrentValue(this as M, newValue)
    }

    /**
     * Can be used to enable or disable persisting the value on the local storage.
     */
    //TODO: Create a Lint warning to enforce overriding the module ID if this property is true.
    val shouldBePersisted: Boolean

    /**
     * Callback triggered when the user modifies the value.
     */
    val onValueChanged: (newValue: T) -> Unit

    override fun createModuleDelegate(): PersistableModuleDelegate<T, M>
}