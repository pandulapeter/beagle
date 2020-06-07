package com.pandulapeter.beagle.core.list.moduleDelegates

import android.animation.ValueAnimator
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.moduleDelegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule

internal class AnimationDurationSwitchDelegate : PersistableModuleDelegate.Boolean<AnimationDurationSwitchModule>() {

    override fun createCells(module: AnimationDurationSwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = module.text,
            isChecked = getCurrentValue(module),
            onValueChanged = { newValue -> setCurrentValue(module, newValue) })
    )

    override fun callOnValueChanged(module: AnimationDurationSwitchModule, newValue: kotlin.Boolean) {
        try {
            ValueAnimator::class.java.methods.firstOrNull { it.name == "setDurationScale" }?.invoke(null, if (newValue) module.multiplier else 1f)
        } catch (_: Throwable) {
        }
        module.onValueChanged(newValue)
    }
}