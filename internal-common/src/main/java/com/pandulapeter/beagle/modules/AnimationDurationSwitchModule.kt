package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.DEFAULT_INITIAL_VALUE
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.DEFAULT_MULTIPLIER
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.DEFAULT_ON_VALUE_CHANGED
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID

/**
 * Displays a switch that, when enabled, increases the duration of animations.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text to display on the switch. The argument of the lambda function is the current value. [DEFAULT_TEXT] by default.
 * @param multiplier - The multiplier that should be applied for all animation durations. [DEFAULT_MULTIPLIER] by default.
 * @param initialValue - Whether or not the switch is checked initially. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched. [DEFAULT_INITIAL_VALUE] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. [DEFAULT_ON_VALUE_CHANGED] by default.
 */
@Suppress("unused")
data class AnimationDurationSwitchModule(
    override val text: (currentValue: Boolean) -> Text = { DEFAULT_TEXT.toText() },
    val multiplier: Float = DEFAULT_MULTIPLIER,
    override val initialValue: Boolean = DEFAULT_INITIAL_VALUE,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    override val onValueChanged: (newValue: Boolean) -> Unit = DEFAULT_ON_VALUE_CHANGED
) : ValueWrapperModule<Boolean, AnimationDurationSwitchModule> {

    override val id: String = ID
    override val shouldRequireConfirmation = false

    companion object {
        const val ID = "animationDurationSwitch"
        private const val DEFAULT_TEXT = "Slow down animations"
        private const val DEFAULT_MULTIPLIER = 4f
        private const val DEFAULT_INITIAL_VALUE = false
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private val DEFAULT_ON_VALUE_CHANGED: (Boolean) -> Unit = {}
    }
}