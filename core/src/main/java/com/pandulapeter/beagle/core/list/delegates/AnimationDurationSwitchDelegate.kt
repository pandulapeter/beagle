package com.pandulapeter.beagle.core.list.delegates

import android.animation.ValueAnimator
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.delegates.shared.ValueWrapperModuleDelegate
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule

internal class AnimationDurationSwitchDelegate : ValueWrapperModuleDelegate.Boolean<AnimationDurationSwitchModule>() {

    override fun createCells(module: AnimationDurationSwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = module.text,
            isChecked = getCurrentValue(module),
            isEnabled = module.isEnabled,
            onValueChanged = { newValue -> setCurrentValue(module, newValue) }
        )
    )

    override fun callOnValueChanged(module: AnimationDurationSwitchModule, newValue: kotlin.Boolean) {
        try {
            ValueAnimator::class.java.methods.firstOrNull { it.name == "setDurationScale" }?.invoke(null, if (newValue) module.multiplier else 1f)
        } catch (_: Throwable) {
        }
        super.callOnValueChanged(module, newValue)
    }
}