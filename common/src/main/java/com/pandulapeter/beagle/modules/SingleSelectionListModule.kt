package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.SingleSelectionListModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY
import com.pandulapeter.beagle.modules.SingleSelectionListModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.SingleSelectionListModule.Companion.DEFAULT_SHOULD_REQUIRE_CONFIRMATION

/**
 * Displays a list of radio buttons represented by [BeagleListItemContract] instances. Only one item is selected at any given time.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param items - The list of items that should be displayed.
 * @param initiallySelectedItemId - The ID of the item that should be selected initially, or null for no initial selection. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the selected value on the local storage. This will only work if the module has a unique, constant ID. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. [DEFAULT_SHOULD_REQUIRE_CONFIRMATION] by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 * @param onSelectionChanged - Callback called when the changes the selection. The parameter is the currently selected item. Empty implementation by default.
 */
data class SingleSelectionListModule<T : BeagleListItemContract>(
    val title: (T?) -> Text,
    val items: List<T>,
    val initiallySelectedItemId: String?,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    override val shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = Module.randomId,
    val onSelectionChanged: (selectedItem: T?) -> Unit = {}
) : ExpandableModule<SingleSelectionListModule<T>>, ValueWrapperModule<String, SingleSelectionListModule<T>> {

    constructor(
        title: CharSequence,
        items: List<T>,
        initiallySelectedItemId: String?,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
        shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = Module.randomId,
        onSelectionChanged: (selectedItem: T?) -> Unit = {}
    ) : this(
        title = { Text.CharSequence(title) },
        items = items,
        initiallySelectedItemId = initiallySelectedItemId,
        isEnabled = isEnabled,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        isExpandedInitially = isExpandedInitially,
        id = id,
        onSelectionChanged = onSelectionChanged
    )

    override val initialValue = initiallySelectedItemId.orEmpty() //TODO: Using an empty string is not a great idea, this should be null
    override val onValueChanged: (newValue: String) -> Unit = { newValue -> onSelectionChanged(newValue.toItem()) }
    override val text: (String) -> Text = { title(it.toItem()) }

    override fun getHeaderTitle(beagle: BeagleContract) = title(getCurrentValue(beagle).toItem())

    private fun String?.toItem() = items.firstOrNull { it.id == this }

    companion object {
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private const val DEFAULT_SHOULD_REQUIRE_CONFIRMATION = false
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}