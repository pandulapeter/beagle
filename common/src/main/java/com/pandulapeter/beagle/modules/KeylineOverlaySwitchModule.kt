package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule.Companion.ID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_APPLY_INSETS
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_COLOR
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_GRID
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_INITIAL_VALUE
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_KEYLINE_PRIMARY
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_KEYLINE_SECONDARY
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_ON_VALUE_CHANGED
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule.Companion.ID

/**
 * Displays a switch that, when enabled, draws a grid over your app with configurable dimensions that you can use to check the alignments of your Views.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param text - The text that appears near the switch. [DEFAULT_TEXT] by default.
 * @param grid - The distance between the grid lines, or null for 8dp. [DEFAULT_GRID] by default.
 * @param keylinePrimary - The distance between the edge of the screen and the primary keyline, or null for 16dp (24dp on tablets). [DEFAULT_KEYLINE_PRIMARY] by default.
 * @param keylineSecondary - The distance between the edge of the screen and the secondary keyline. or null for 72dp (80dp on tablets). [DEFAULT_KEYLINE_SECONDARY] by default.
 * @param color - The color to be used when drawing the grid, or null for the theme's text color. [DEFAULT_COLOR] by default.
 * @param initialValue - Whether or not the switch is checked initially. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched. [DEFAULT_INITIAL_VALUE] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param applyInsets - The library tries to handle window insets the best it can, but this might not work with your specific setup. To override the default behavior, provide a lambda that returns a new [Insets] object. [DEFAULT_APPLY_INSETS] by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. [DEFAULT_ON_VALUE_CHANGED] by default.
 */
data class KeylineOverlaySwitchModule(
    override val text: (Boolean) -> Text = { Text.CharSequence(DEFAULT_TEXT) },
    @Dimension val grid: Int? = DEFAULT_GRID,
    @Dimension val keylinePrimary: Int? = DEFAULT_KEYLINE_PRIMARY,
    @Dimension val keylineSecondary: Int? = DEFAULT_KEYLINE_SECONDARY,
    @ColorInt val color: Int? = DEFAULT_COLOR,
    override val initialValue: Boolean = DEFAULT_INITIAL_VALUE,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    val applyInsets: ((windowInsets: Insets) -> Insets)? = DEFAULT_APPLY_INSETS,
    override val onValueChanged: (Boolean) -> Unit = DEFAULT_ON_VALUE_CHANGED
) : ValueWrapperModule<Boolean, KeylineOverlaySwitchModule> {

    override val id: String = ID
    override val shouldRequireConfirmation = false

    companion object {
        const val ID = "keylineOverlaySwitch"
        private const val DEFAULT_TEXT = "Keyline overlay"
        private val DEFAULT_GRID: Int? = null
        private val DEFAULT_KEYLINE_PRIMARY: Int? = null
        private val DEFAULT_KEYLINE_SECONDARY: Int? = null
        private val DEFAULT_COLOR: Int? = null
        private const val DEFAULT_INITIAL_VALUE = false
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private val DEFAULT_APPLY_INSETS: ((windowInsets: Insets) -> Insets)? = null
        private val DEFAULT_ON_VALUE_CHANGED: (Boolean) -> Unit = {}
    }
}