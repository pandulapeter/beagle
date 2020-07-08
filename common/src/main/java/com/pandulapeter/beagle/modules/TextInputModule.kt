package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import java.util.UUID

/**
 * Allows the user to enter a text value.
 *
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). Optional, random string by default.
 * @param text - A lambda that returns the name that should appear on the module in function of its current value.
 * @param validator - A lambda that returns whether or not its input is a valid value for this module. Optional, by default it only checks if the String is not blank.
 * @param initialValue - The initial value of the text. Optional, empty string by default. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param onValueChanged - Callback triggered when the user changes the current value. In case of persisted values, this will also get called the first time the module is added.
 */
data class TextInputModule(
    override val id: String = UUID.randomUUID().toString(),
    val text: (String) -> CharSequence,
    val validator: (String) -> Boolean = { it.isNotBlank() },
    override val initialValue: String = "",
    override val isValuePersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val onValueChanged: (String) -> Unit
) : ValueWrapperModule<String, TextInputModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}