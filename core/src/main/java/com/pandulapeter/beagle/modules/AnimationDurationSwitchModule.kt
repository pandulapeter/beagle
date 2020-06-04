package com.pandulapeter.beagle.modules

import android.animation.ValueAnimator
import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.builtIn.AnimationDurationSwitchModuleContract

/**
 * Displays a switch that, when enabled, increases the duration of animations.
 * This module can only be added once. It uses the value of [AnimationDurationSwitchModuleContract.ID] as id.
 *
 * @param text - The text to display on the switch. Optional, "Slow down animations" by default.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param shouldBePersisted - Can be used to enable or disable persisting the value on the local storage. Optional, false by default.
 * @param multiplier - The multiplier that should be applied for all animation durations. Optional, 4f by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. Optional, empty implementation by default.
 */
class AnimationDurationSwitchModule(
    text: CharSequence = "Slow down animations",
    @ColorInt color: Int? = null,
    shouldBePersisted: Boolean = false,
    override val multiplier: Float = 4f,
    onValueChanged: (Boolean) -> Unit = {}
) : AnimationDurationSwitchModuleContract, SwitchModule(
    id = AnimationDurationSwitchModuleContract.ID,
    color = color,
    text = text,
    shouldBePersisted = shouldBePersisted,
    onValueChanged = { newValue ->
        try {
            ValueAnimator::class.java.methods.firstOrNull { it.name == "setDurationScale" }?.invoke(null, if (newValue) multiplier else 1f)
        } catch (_: Throwable) {
        }
        onValueChanged(newValue)
    }
)