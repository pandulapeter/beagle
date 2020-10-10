package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import java.util.UUID

/**
 * Displays a simple switch.
 *
 * @param text - The text to display on the switch.
 * @param initialValue - Whether or not the switch is checked initially. Optional, false by default. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. Optional, true by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). Optional, random string by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. Empty implementation by default.
 */
data class SwitchModule(
    override val text: (Boolean) -> Text,
    override val initialValue: Boolean = false,
    override val isEnabled: Boolean = true,
    override val isValuePersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val id: String = UUID.randomUUID().toString(),
    override val onValueChanged: (Boolean) -> Unit = {}
) : ValueWrapperModule<Boolean, SwitchModule> {

    constructor(
        text: CharSequence,
        initialValue: Boolean = false,
        isEnabled: Boolean = true,
        isValuePersisted: Boolean = false,
        shouldRequireConfirmation: Boolean = false,
        id: String = UUID.randomUUID().toString(),
        onValueChanged: (Boolean) -> Unit = {}
    ) : this(
        text = { Text.CharSequence(text) },
        initialValue = initialValue,
        isEnabled = isEnabled,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        id = id,
        onValueChanged = onValueChanged
    )

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}