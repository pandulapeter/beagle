package com.pandulapeter.beagleCore.configuration.modules

import com.pandulapeter.beagleCore.contracts.BeagleModuleContract
import java.util.UUID

/**
 * Displays a switch with configurable title and behavior - ideal for feature toggles.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
 * @param title - The text that appears near the switch. "Keyline overlay" by default.
 * @param initialValue - The initial value of the toggle. False by default.
 * @param onValueChanged - Callback that gets invoked when the user changes the value of the toggle.
 */
data class ToggleModule(
    override val id: String = UUID.randomUUID().toString(),
    val title: CharSequence,
    val initialValue: Boolean = false,
    val onValueChanged: (newValue: Boolean) -> Unit
) : BeagleModuleContract