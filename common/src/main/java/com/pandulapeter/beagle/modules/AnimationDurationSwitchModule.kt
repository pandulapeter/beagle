package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.PersistableModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID

/**
 * Displays a switch that, when enabled, increases the duration of animations.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text to display on the switch. Optional, "Slow down animations" by default.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param shouldBePersisted - Can be used to enable or disable persisting the value on the local storage. Optional, false by default.
 * @param multiplier - The multiplier that should be applied for all animation durations. Optional, 4f by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. TODO: In case of persisted values, this will also get called the first time the module is added. Optional, empty implementation by default.
 */
data class AnimationDurationSwitchModule(
    val text: CharSequence = "Slow down animations",
    @ColorInt val color: Int? = null,
    override val shouldBePersisted: Boolean = false,
    val multiplier: Float = 4f,
    override val onValueChanged: ((Boolean) -> Unit) = {}
) : PersistableModule<Boolean, AnimationDurationSwitchModule> {

    override val id: String = ID
    override val initialValue: Boolean = false

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "animationDurationSwitch"
    }
}