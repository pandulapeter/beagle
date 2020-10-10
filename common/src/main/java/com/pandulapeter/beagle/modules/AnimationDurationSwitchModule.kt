package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID

/**
 * Displays a switch that, when enabled, increases the duration of animations.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text to display on the switch. Optional, "Slow down animations" by default.
 * @param multiplier - The multiplier that should be applied for all animation durations. Optional, 4f by default.
 * @param initialValue - Whether or not the switch is checked initially. Optional, false by default. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. Optional, true by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. Optional, false by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. Optional, empty implementation by default.
 */
data class AnimationDurationSwitchModule(
    val text: Text = Text.CharSequence("Slow down animations"),
    val multiplier: Float = 4f,
    override val initialValue: Boolean = false,
    override val isValuePersisted: Boolean = false,
    override val isEnabled: Boolean = true,
    override val onValueChanged: (Boolean) -> Unit = {}
) : ValueWrapperModule<Boolean, AnimationDurationSwitchModule> {

    override val id: String = ID
    override val shouldRequireConfirmation = false

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "animationDurationSwitch"
    }
}