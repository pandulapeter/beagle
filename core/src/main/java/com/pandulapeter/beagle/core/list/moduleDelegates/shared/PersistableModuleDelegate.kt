package com.pandulapeter.beagle.core.list.moduleDelegates.shared

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.PersistableModule

internal abstract class PersistableModuleDelegate<T, M : PersistableModule<T, M>> : PersistableModule.Delegate<T, M> {

    protected var hasCalledListenerForTheFirstTime = false

    protected open fun callOnValueChanged(module: M, newValue: T) = module.onValueChanged(newValue)

    abstract class Boolean<M : PersistableModule<kotlin.Boolean, M>> : PersistableModuleDelegate<kotlin.Boolean, M>() {

        final override fun getCurrentValue(module: M) = if (module.shouldBePersisted) {
            (BeagleCore.implementation.localStorageManager.booelans[module.id] ?: module.initialValue).also { value ->
                if (!hasCalledListenerForTheFirstTime && BeagleCore.implementation.currentActivity != null) {
                    callOnValueChanged(module, value)
                    hasCalledListenerForTheFirstTime = true
                }
            }
        } else {
            BeagleCore.implementation.memoryStorageManager.booleans[module.id] ?: module.initialValue
        }

        final override fun setCurrentValue(module: M, newValue: kotlin.Boolean) {
            if (newValue != getCurrentValue(module)) {
                if (module.shouldBePersisted) {
                    BeagleCore.implementation.localStorageManager.booelans[module.id] = newValue
                } else {
                    BeagleCore.implementation.memoryStorageManager.booleans[module.id] = newValue
                }
                BeagleCore.implementation.refreshCells()
                callOnValueChanged(module, newValue)
            }
        }
    }

    abstract class String<M : PersistableModule<kotlin.String, M>> : PersistableModuleDelegate<kotlin.String, M>() {

        final override fun getCurrentValue(module: M) = if (module.shouldBePersisted) {
            (BeagleCore.implementation.localStorageManager.strings[module.id] ?: module.initialValue).also { value ->
                if (!hasCalledListenerForTheFirstTime && BeagleCore.implementation.currentActivity != null) {
                    callOnValueChanged(module, value)
                    hasCalledListenerForTheFirstTime = true
                }
            }
        } else {
            BeagleCore.implementation.memoryStorageManager.strings[module.id] ?: module.initialValue
        }

        final override fun setCurrentValue(module: M, newValue: kotlin.String) {
            if (newValue != getCurrentValue(module)) {
                if (module.shouldBePersisted) {
                    BeagleCore.implementation.localStorageManager.strings[module.id] = newValue
                } else {
                    BeagleCore.implementation.memoryStorageManager.strings[module.id] = newValue
                }
                BeagleCore.implementation.refreshCells()
                callOnValueChanged(module, newValue)
            }
        }
    }
}