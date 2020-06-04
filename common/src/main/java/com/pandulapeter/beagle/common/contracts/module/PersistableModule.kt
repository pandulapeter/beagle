package com.pandulapeter.beagle.common.contracts.module

/**
 * Modules that wrap primitives that can be persisted in SharedPreferences must implement this interface.
 * The save-load functionality is optional and will only work if the id of the module is constant across app sessions.
 */
interface PersistableModule<T> : Module {

    /**
     * The initial value.
     * If [shouldBePersisted] is true, the value coming from the local storage will override this field so it will only be used the first time the app is launched.
     */
    val initialValue: T

    /**
     * The current value. Can be queried at any time.
     */
    val currentValue: T

    /**
     * Can be used to enable or disable persisting the value on the local storage.
     */
    //TODO: Create a Lint warning to enforce overriding the module ID if this property is true.
    val shouldBePersisted: Boolean

    /**
     * Callback triggered when the user modifies the value.
     */
    val onValueChanged: (newValue: T) -> Unit
}