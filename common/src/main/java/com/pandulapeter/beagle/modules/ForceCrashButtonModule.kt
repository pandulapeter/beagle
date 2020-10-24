package com.pandulapeter.beagle.modules

import androidx.annotation.DrawableRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_EXCEPTION_MESSAGE
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_ICON
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_ON_BUTTON_PRESSED
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.DEFAULT_TYPE
import com.pandulapeter.beagle.modules.ForceCrashButtonModule.Companion.ID


/**
 * Displays a button that throws an exception when pressed - useful for testing crash reporting.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. [DEFAULT_TEXT] by default.
 * @param exception - The [RuntimeException] to throw. The default value has the [DEFAULT_EXCEPTION_MESSAGE] message.
 * @param type - Specify a [TextModule.Type] to apply a specific appearance. [DEFAULT_TYPE] by default.
 * @param icon - A drawable resource ID that will be tinted and displayed before the text, or null to display no icon. [DEFAULT_ICON] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param onButtonPressed - Callback invoked when the user presses the button. [DEFAULT_ON_BUTTON_PRESSED] by default.
 */
@Suppress("unused")
data class ForceCrashButtonModule(
    val text: Text = DEFAULT_TEXT.toText(),
    val exception: RuntimeException = RuntimeException(DEFAULT_EXCEPTION_MESSAGE),
    val type: TextModule.Type = DEFAULT_TYPE,
    @DrawableRes val icon: Int? = DEFAULT_ICON,
    val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    val onButtonPressed: () -> Unit = DEFAULT_ON_BUTTON_PRESSED
) : Module<ForceCrashButtonModule> {

    override val id: String = ID

    companion object {
        const val ID = "forceCrashButton"
        private const val DEFAULT_TEXT = "Force crash"
        private const val DEFAULT_EXCEPTION_MESSAGE = "Test crash"
        private val DEFAULT_TYPE = TextModule.Type.NORMAL
        private val DEFAULT_ICON: Int? = null
        private const val DEFAULT_IS_ENABLED = true
        private val DEFAULT_ON_BUTTON_PRESSED: () -> Unit = {}
    }
}