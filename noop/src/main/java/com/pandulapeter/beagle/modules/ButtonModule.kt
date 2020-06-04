package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.builtIn.ButtonModuleContract

/**
 * Empty implementation.
 */
@Suppress("unused")
open class ButtonModule(
    override val id: String = "",
    override val text: CharSequence = "",
    @ColorInt override val color: Int? = null,
    override val onButtonPressed: () -> Unit = {}
) : ButtonModuleContract