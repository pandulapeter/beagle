package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID


/**
 * Displays a button that links to the Android App Info page for your app.
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that should be displayed on the button. Optional, "Show app info" by default.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param shouldOpenInNewTask - Whether or not the App Info page will be opened with the Intent.FLAG_ACTIVITY_NEW_TASK flag. False by default.
 * @param onButtonPressed - Callback called when the user presses the button. Optional, empty implementation by default.
 */
data class AppInfoButtonModule(
    val text: CharSequence = "Show app info",
    @ColorInt val color: Int? = null,
    val shouldOpenInNewTask: Boolean = false,
    val onButtonPressed: () -> Unit = {}
) : Module<AppInfoButtonModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "appInfoButton"
    }
}