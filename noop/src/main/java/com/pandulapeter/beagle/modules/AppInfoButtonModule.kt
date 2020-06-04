package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.builtIn.AppInfoButtonModuleContract

/**
 * Empty implementation.
 */
@Suppress("unused")
class AppInfoButtonModule(
    override val text: CharSequence = "",
    @ColorInt override val color: Int? = null,
    override val shouldOpenInNewTask: Boolean = false
) : AppInfoButtonModuleContract {

    override val id: String = ""
    override val onButtonPressed: () -> Unit = {}
}