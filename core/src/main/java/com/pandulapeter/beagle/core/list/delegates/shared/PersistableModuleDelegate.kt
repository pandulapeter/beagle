package com.pandulapeter.beagle.core.list.delegates.shared

import androidx.annotation.CallSuper
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule

internal abstract class PersistableModuleDelegate<T, M : ValueWrapperModule<T, M>> : ValueWrapperModule.Delegate<T, M> {

    private var hasCalledListenerForTheFirstTime = false
    private val pendingUpdates = mutableListOf<PendingChangeEvent<T>>()
    private val uiValues = mutableMapOf<kotlin.String, T>()

    fun getUiValue(module: M) = if (module.isValuePersisted && module.shouldRequireConfirmation) (uiValues[module.id] ?: getCurrentValue(module)) else getCurrentValue(module)

    fun setUiValue(module: M, newValue: T) {
        if (module.shouldRequireConfirmation) {
            pendingUpdates.removeAll { it.moduleId == module.id }
            if (getCurrentValue(module) == newValue) {
                uiValues.remove(module.id)
            } else {
                uiValues[module.id] = newValue
                pendingUpdates.add(
                    PendingChangeEvent(
                        moduleId = module.id,
                        pendingValue = newValue
                    )
                )
            }
            BeagleCore.implementation.refresh()
        } else if (hasValueChanged(newValue, module)) {
            setCurrentValue(module, newValue)
        }
    }

    protected fun hasValueChanged(newValue: T, module: M) = newValue != getUiValue(module)

    override fun hasPendingChanges(module: M) = module.shouldRequireConfirmation && pendingUpdates.any { it.moduleId == module.id }

    override fun applyPendingChanges(module: M) {
        pendingUpdates.indexOfFirst { it.moduleId == module.id }.let { index ->
            if (index != -1) {
                uiValues.remove(module.id)
                setCurrentValue(module, pendingUpdates[index].pendingValue)
                pendingUpdates.removeAt(index)
            }
        }
    }

    override fun resetPendingChanges(module: M) {
        pendingUpdates.removeAll { it.moduleId == module.id }
        uiValues.remove(module.id)
        getCurrentValue(module)?.let { setCurrentValue(module, it) }
    }

    protected fun callListenerForTheFirstTimeIfNeeded(module: M, value: T) {
        if (module.isValuePersisted && !hasCalledListenerForTheFirstTime && BeagleCore.implementation.currentActivity != null) {
            hasCalledListenerForTheFirstTime = true
            callOnValueChanged(module, value)
        }
    }

    @CallSuper
    protected open fun callOnValueChanged(module: M, newValue: T) = module.onValueChanged(newValue)

    abstract class Boolean<M : ValueWrapperModule<kotlin.Boolean, M>> : PersistableModuleDelegate<kotlin.Boolean, M>() {

        final override fun getCurrentValue(module: M) = if (module.isValuePersisted) {
            (BeagleCore.implementation.localStorageManager.booelans[module.id] ?: module.initialValue).also { value ->
                callListenerForTheFirstTimeIfNeeded(module, value)
            }
        } else {
            BeagleCore.implementation.memoryStorageManager.booleans[module.id] ?: module.initialValue
        }

        final override fun setCurrentValue(module: M, newValue: kotlin.Boolean) {
            if (hasValueChanged(newValue, module)) {
                if (module.isValuePersisted) {
                    BeagleCore.implementation.localStorageManager.booelans[module.id] = newValue
                } else {
                    BeagleCore.implementation.memoryStorageManager.booleans[module.id] = newValue
                }
                BeagleCore.implementation.refresh()
                callOnValueChanged(module, newValue)
            }
        }
    }

    abstract class Integer<M : ValueWrapperModule<Int, M>> : PersistableModuleDelegate<Int, M>() {

        final override fun getCurrentValue(module: M) = if (module.isValuePersisted) {
            (BeagleCore.implementation.localStorageManager.integers[module.id] ?: module.initialValue).also { value ->
                callListenerForTheFirstTimeIfNeeded(module, value)
            }
        } else {
            BeagleCore.implementation.memoryStorageManager.integers[module.id] ?: module.initialValue
        }

        final override fun setCurrentValue(module: M, newValue: Int) {
            if (hasValueChanged(newValue, module)) {
                if (module.isValuePersisted) {
                    BeagleCore.implementation.localStorageManager.integers[module.id] = newValue
                } else {
                    BeagleCore.implementation.memoryStorageManager.integers[module.id] = newValue
                }
                BeagleCore.implementation.refresh()
                callOnValueChanged(module, newValue)
            }
        }
    }

    abstract class String<M : ValueWrapperModule<kotlin.String, M>> : PersistableModuleDelegate<kotlin.String, M>() {

        final override fun getCurrentValue(module: M) = if (module.isValuePersisted) {
            (BeagleCore.implementation.localStorageManager.strings[module.id] ?: module.initialValue).also { value ->
                callListenerForTheFirstTimeIfNeeded(module, value)
            }
        } else {
            BeagleCore.implementation.memoryStorageManager.strings[module.id] ?: module.initialValue
        }

        final override fun setCurrentValue(module: M, newValue: kotlin.String) {
            if (hasValueChanged(newValue, module)) {
                if (module.isValuePersisted) {
                    BeagleCore.implementation.localStorageManager.strings[module.id] = newValue
                } else {
                    BeagleCore.implementation.memoryStorageManager.strings[module.id] = newValue
                }
                BeagleCore.implementation.refresh()
                callOnValueChanged(module, newValue)
            }
        }
    }

    abstract class StringSet<M : ValueWrapperModule<Set<kotlin.String>, M>> : PersistableModuleDelegate<Set<kotlin.String>, M>() {

        final override fun getCurrentValue(module: M) = if (module.isValuePersisted) {
            (BeagleCore.implementation.localStorageManager.stringSets[module.id] ?: module.initialValue).also { value ->
                callListenerForTheFirstTimeIfNeeded(module, value)
            }
        } else {
            BeagleCore.implementation.memoryStorageManager.stringSets[module.id] ?: module.initialValue
        }

        final override fun setCurrentValue(module: M, newValue: Set<kotlin.String>) {
            if (hasValueChanged(newValue, module)) {
                if (module.isValuePersisted) {
                    BeagleCore.implementation.localStorageManager.stringSets[module.id] = newValue
                } else {
                    BeagleCore.implementation.memoryStorageManager.stringSets[module.id] = newValue
                }
                BeagleCore.implementation.refresh()
                callOnValueChanged(module, newValue)
            }
        }
    }

    data class PendingChangeEvent<T>(
        val moduleId: kotlin.String,
        val pendingValue: T
    )
}