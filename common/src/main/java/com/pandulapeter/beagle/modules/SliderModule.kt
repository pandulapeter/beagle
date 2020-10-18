package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.modules.SliderModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.SliderModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.SliderModule.Companion.DEFAULT_MAXIMUM_VALUE
import com.pandulapeter.beagle.modules.SliderModule.Companion.DEFAULT_MINIMUM_VALUE
import com.pandulapeter.beagle.modules.SliderModule.Companion.DEFAULT_ON_VALUE_CHANGED
import com.pandulapeter.beagle.modules.SliderModule.Companion.DEFAULT_SHOULD_REQUIRE_CONFIRMATION

/**
 * Allows the user to adjust a numeric value.
 *
 * @param text - A lambda that returns the name that should appear above the slider in function of its current value.
 * @param minimumValue - The smallest value supported by the slider. [DEFAULT_MINIMUM_VALUE] by default.
 * @param maximumValue - The largest value supported by the slider. [DEFAULT_MAXIMUM_VALUE] by default.
 * @param initialValue - The initial value of the slider. By default it's the same as the slider's minimum value. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. [DEFAULT_SHOULD_REQUIRE_CONFIRMATION] by default.
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). [randomId] by default.
 * @param onValueChanged - Callback triggered when the user changes the current value. In case of persisted values, this will also get called the first time the module is added. [DEFAULT_ON_VALUE_CHANGED] by default.
 */
@Suppress("unused")
data class SliderModule(
    override val text: (currentValue: Int) -> Text,
    val minimumValue: Int = DEFAULT_MINIMUM_VALUE,
    val maximumValue: Int = DEFAULT_MAXIMUM_VALUE,
    override val initialValue: Int = minimumValue,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    override val shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
    override val id: String = randomId,
    override val onValueChanged: (newValue: Int) -> Unit = DEFAULT_ON_VALUE_CHANGED
) : ValueWrapperModule<Int, SliderModule> {

    constructor(
        text: CharSequence,
        minimumValue: Int = DEFAULT_MINIMUM_VALUE,
        maximumValue: Int = DEFAULT_MAXIMUM_VALUE,
        initialValue: Int = minimumValue,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
        shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
        id: String = randomId,
        onValueChanged: (Int) -> Unit = DEFAULT_ON_VALUE_CHANGED
    ) : this(
        text = { Text.CharSequence(text) },
        minimumValue = minimumValue,
        maximumValue = maximumValue,
        initialValue = initialValue,
        isEnabled = isEnabled,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        id = id,
        onValueChanged = onValueChanged
    )

    constructor(
        @StringRes text: Int,
        minimumValue: Int = DEFAULT_MINIMUM_VALUE,
        maximumValue: Int = DEFAULT_MAXIMUM_VALUE,
        initialValue: Int = minimumValue,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
        shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
        id: String = randomId,
        onValueChanged: (Int) -> Unit = DEFAULT_ON_VALUE_CHANGED
    ) : this(
        text = { Text.ResourceId(text) },
        minimumValue = minimumValue,
        maximumValue = maximumValue,
        initialValue = initialValue,
        isEnabled = isEnabled,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        id = id,
        onValueChanged = onValueChanged
    )

    companion object {
        private const val DEFAULT_MINIMUM_VALUE = 0
        private const val DEFAULT_MAXIMUM_VALUE = 10
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private const val DEFAULT_SHOULD_REQUIRE_CONFIRMATION = false
        private val DEFAULT_ON_VALUE_CHANGED: (Int) -> Unit = {}
    }
}