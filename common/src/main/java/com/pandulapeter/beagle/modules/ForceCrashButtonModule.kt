package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.ID


/**
 * Displays a button that throws an exception when pressed - useful for testing crash reporting.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. "Force crash" by default.
 * @param message - The detail message of the exception. "Test crash: Beagle was a bad boy." by default.
 * @param onButtonPressed - Callback called when the user presses the button. Optional, empty implementation by default.
 */
data class ForceCrashButtonModule(
    val text: CharSequence = "Force crash",
    val message: String = "Test crash: Beagle was a bad boy.",
    val onButtonPressed: () -> Unit = {}
) : Module<ForceCrashButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "forceCrashButton"
    }
}