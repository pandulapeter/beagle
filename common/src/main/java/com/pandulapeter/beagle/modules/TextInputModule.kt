package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import java.util.UUID

/**
 * Allows the user to enter a text value.
 *
 * @param text - A lambda that returns the name that should appear on the module in function of its current value.
 * @param initialValue - The initial value of the text. Optional, empty string by default. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param areRealTimeUpdatesEnabled - If true, a new value will be set after every text change, otherwise this is only done when the user closes the dialog. True by default.
 * @param doneText - The text that appears on the positive button of the dialog. "Done" by default.
 * @param cancelText - The text that appears on the negative button of the dialog. This button is only displayed if the value of [areRealTimeUpdatesEnabled] is false. "Cancel" by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). Optional, random string by default.
 * @param validator - A lambda that returns whether or not its input is a valid value for this module. Optional, by default it accepts any String.
 * @param onValueChanged - Callback triggered when the user changes the current value. In case of persisted values, this will also get called the first time the module is added. Empty implementation by default.
 */
//TODO: Add parameters to customize EditText hint and input type.
data class TextInputModule(
    val text: (String) -> CharSequence,
    override val initialValue: String = "",
    val areRealTimeUpdatesEnabled: Boolean = true,
    val doneText: CharSequence = "Done",
    val cancelText: CharSequence = "Cancel",
    override val isValuePersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val id: String = UUID.randomUUID().toString(),
    val validator: (String) -> Boolean = { true },
    override val onValueChanged: (String) -> Unit = {}
) : ValueWrapperModule<String, TextInputModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}