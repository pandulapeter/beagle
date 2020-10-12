package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_ARE_REAL_TIME_UPDATES_ENABLED
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_CANCEL_TEXT
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_DONE_TEXT
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_INITIAL_VALUE
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_ON_VALUE_CHANGED
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_SHOULD_REQUIRE_CONFIRMATION
import com.pandulapeter.beagle.modules.TextInputModule.Companion.DEFAULT_VALIDATOR

/**
 * Allows the user to enter a text value.
 *
 * @param text - A lambda that returns the name that should appear on the module in function of its current value.
 * @param initialValue - The initial value of the text. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched. [DEFAULT_INITIAL_VALUE] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param areRealTimeUpdatesEnabled - If true, a new value will be set after every text change, otherwise this is only done when the user closes the dialog. [DEFAULT_ARE_REAL_TIME_UPDATES_ENABLED] by default.
 * @param doneText - The text that appears on the positive button of the dialog. [DEFAULT_DONE_TEXT] by default.
 * @param cancelText - The text that appears on the negative button of the dialog. This button is only displayed if the value of [areRealTimeUpdatesEnabled] is false. [DEFAULT_CANCEL_TEXT] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. [DEFAULT_SHOULD_REQUIRE_CONFIRMATION] by default.
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). [Module.randomId] by default.
 * @param validator - A lambda that returns whether or not its argument is a valid value for this module. [DEFAULT_VALIDATOR] by default.
 * @param onValueChanged - Callback triggered when the user changes the current value. In case of persisted values, this will also get called the first time the module is added. [DEFAULT_ON_VALUE_CHANGED] by default.
 */
@Suppress("unused")
data class TextInputModule(
    override val text: (String) -> Text,
    override val initialValue: String = DEFAULT_INITIAL_VALUE,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    val areRealTimeUpdatesEnabled: Boolean = DEFAULT_ARE_REAL_TIME_UPDATES_ENABLED,
    val doneText: Text = Text.CharSequence(DEFAULT_DONE_TEXT),
    val cancelText: Text = Text.CharSequence(DEFAULT_CANCEL_TEXT),
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    override val shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
    override val id: String = Module.randomId,
    val validator: (String) -> Boolean = DEFAULT_VALIDATOR,
    override val onValueChanged: (String) -> Unit = DEFAULT_ON_VALUE_CHANGED
) : ValueWrapperModule<String, TextInputModule> {

    constructor(
        text: CharSequence,
        initialValue: String = DEFAULT_INITIAL_VALUE,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        areRealTimeUpdatesEnabled: Boolean = DEFAULT_ARE_REAL_TIME_UPDATES_ENABLED,
        doneText: Text = Text.CharSequence(DEFAULT_DONE_TEXT),
        cancelText: Text = Text.CharSequence(DEFAULT_CANCEL_TEXT),
        isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
        shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
        id: String = Module.randomId,
        validator: (String) -> Boolean = DEFAULT_VALIDATOR,
        onValueChanged: (String) -> Unit = DEFAULT_ON_VALUE_CHANGED
    ) : this(
        text = { Text.CharSequence(text) },
        initialValue = initialValue,
        isEnabled = isEnabled,
        areRealTimeUpdatesEnabled = areRealTimeUpdatesEnabled,
        doneText = doneText,
        cancelText = cancelText,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        id = id,
        validator = validator,
        onValueChanged = onValueChanged
    )

    constructor(
        @StringRes text: Int,
        initialValue: String = DEFAULT_INITIAL_VALUE,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        areRealTimeUpdatesEnabled: Boolean = DEFAULT_ARE_REAL_TIME_UPDATES_ENABLED,
        doneText: Text = Text.CharSequence(DEFAULT_DONE_TEXT),
        cancelText: Text = Text.CharSequence(DEFAULT_CANCEL_TEXT),
        isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
        shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
        id: String = Module.randomId,
        validator: (String) -> Boolean = DEFAULT_VALIDATOR,
        onValueChanged: (String) -> Unit = DEFAULT_ON_VALUE_CHANGED
    ) : this(
        text = { Text.ResourceId(text) },
        initialValue = initialValue,
        isEnabled = isEnabled,
        areRealTimeUpdatesEnabled = areRealTimeUpdatesEnabled,
        doneText = doneText,
        cancelText = cancelText,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        id = id,
        validator = validator,
        onValueChanged = onValueChanged
    )

    companion object {
        private const val DEFAULT_INITIAL_VALUE = ""
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_ARE_REAL_TIME_UPDATES_ENABLED = true
        private const val DEFAULT_DONE_TEXT = "Done"
        private const val DEFAULT_CANCEL_TEXT = "Cancel"
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private const val DEFAULT_SHOULD_REQUIRE_CONFIRMATION = false
        private val DEFAULT_VALIDATOR: (String) -> Boolean = { true }
        private val DEFAULT_ON_VALUE_CHANGED: (String) -> Unit = {}
    }
}