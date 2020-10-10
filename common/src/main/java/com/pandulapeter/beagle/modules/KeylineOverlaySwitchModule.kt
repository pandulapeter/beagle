package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID

/**
 * Displays a switch that, when enabled, draws a grid over your app with configurable dimensions that you can use to check the alignments of your Views.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that appears near the switch. "Keyline overlay" by default.
 * @param grid - The distance between the grid lines. 8dp by default.
 * @param keylinePrimary - The distance between the edge of the screen and the primary keyline. 16dp by default (24dp on tablets).
 * @param keylineSecondary - The distance between the edge of the screen and the secondary keyline. 72dp by default (80dp on tablets).
 * @param color - The color to be used when drawing the grid. It defaults to the theme's text color.
 * @param initialValue - Whether or not the switch is checked initially. Optional, false by default. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. Optional, true by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. Optional, false by default.
 * @param applyInsets - The library tries to handle window insets the best it can, but this might not work with your specific setup. To override the default behavior, provide a lambda that returns a new [Insets] object. Null by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. Optional, empty implementation by default.
 */
data class KeylineOverlaySwitchModule(
    override val text: (Boolean) -> Text = { Text.CharSequence("Keyline overlay") },
    @Dimension val grid: Int? = null,
    @Dimension val keylinePrimary: Int? = null,
    @Dimension val keylineSecondary: Int? = null,
    @ColorInt val color: Int? = null,
    override val initialValue: Boolean = false,
    override val isEnabled: Boolean = true,
    override val isValuePersisted: Boolean = false,
    val applyInsets: ((windowInsets: Insets) -> Insets)? = null,
    override val onValueChanged: (Boolean) -> Unit = {}
) : ValueWrapperModule<Boolean, KeylineOverlaySwitchModule> {

    override val id: String = ID
    override val shouldRequireConfirmation = false

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "keylineOverlaySwitch"
    }
}