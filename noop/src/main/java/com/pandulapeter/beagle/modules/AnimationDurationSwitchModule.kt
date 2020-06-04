package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.builtIn.AnimationDurationSwitchModuleContract

/**
 * Empty implementation.
 */
@Suppress("unused")
class AnimationDurationSwitchModule(
    override val text: CharSequence = "",
    @ColorInt override val color: Int? = null,
    override val shouldBePersisted: Boolean = false,
    override val multiplier: Float = 0f
) : AnimationDurationSwitchModuleContract {

    override val id: String = ""
    override val initialValue: Boolean = false
    override val currentValue: Boolean = false
    override val onValueChanged: (newValue: Boolean) -> Unit = {}
}