package com.pandulapeter.beagle.core.list.moduleDelegates.shared

import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.PersistableModule

internal abstract class PersistableModuleDelegate<T, M : PersistableModule<T, M>> : PersistableModule.Delegate<T, M>() {

    abstract class Boolean<M : PersistableModule<kotlin.Boolean, M>> : PersistableModuleDelegate<kotlin.Boolean, M>() {

        final override fun getCurrentValue(module: M) = if (module.shouldBePersisted) {
            BeagleCore.implementation.localStorageManager.booelans[module.id] ?: module.initialValue
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
                BeagleCore.implementation.updateCells()
                module.onValueChanged(newValue)
            }
        }
    }
}