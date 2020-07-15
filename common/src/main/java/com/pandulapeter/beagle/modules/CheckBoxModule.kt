package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import java.util.UUID

/**
 * Displays a simple check box.
 *
 * @param text - The text to display on the switch.
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). Optional, random string by default.
 * @param initialValue - Whether or not the checkbox is checked initially. Optional, false by default. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added.
 */
data class CheckBoxModule(
    val text: CharSequence,
    override val id: String = UUID.randomUUID().toString(),
    override val initialValue: Boolean = false,
    override val isValuePersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val onValueChanged: (Boolean) -> Unit
) : ValueWrapperModule<Boolean, CheckBoxModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}