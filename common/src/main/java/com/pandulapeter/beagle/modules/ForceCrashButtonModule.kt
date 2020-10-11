package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_EXCEPTION_MESSAGE
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.ID


/**
 * Displays a button that throws an exception when pressed - useful for testing crash reporting.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param exception - The [RuntimeException] to throw. The default value has the [DEFAULT_EXCEPTION_MESSAGE] message.
 * @param onButtonPressed - Callback called when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 */
data class ForceCrashButtonModule(
    val text: Text = Text.CharSequence(DEFAULT_TEXT),
    val exception: RuntimeException = RuntimeException(DEFAULT_EXCEPTION_MESSAGE),
    val onButtonPressed: () -> Unit = DEFAULT_ON_BUTTON_PRESSED
) : Module<ForceCrashButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "forceCrashButton"
        private const val DEFAULT_TEXT = "Force crash"
        private const val DEFAULT_EXCEPTION_MESSAGE = "Test crash"
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
    }
}