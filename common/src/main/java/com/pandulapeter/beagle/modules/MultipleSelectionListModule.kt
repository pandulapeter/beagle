package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.MultipleSelectionListModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY
import com.pandulapeter.beagle.modules.MultipleSelectionListModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.MultipleSelectionListModule.Companion.DEFAULT_SHOULD_REQUIRE_CONFIRMATION

/**
 * Displays a list of check boxes represented by [BeagleListItemContract] instances. Any number of items can be selected at any given time.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param items - The list of items that should be displayed.
 * @param initiallySelectedItemIds - The ID-s of the items that should be selected initially. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the selected value on the local storage. This will only work if the module has a unique, constant ID. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. [DEFAULT_SHOULD_REQUIRE_CONFIRMATION] by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 * @param onSelectionChanged - Callback invoked when the selection is changed. The parameter is a set containing the currently selected items. Empty implementation by default.
 */
data class MultipleSelectionListModule<T : BeagleListItemContract>(
    val title: (Set<T>) -> Text,
    val items: List<T>,
    val initiallySelectedItemIds: Set<String>,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    override val shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = Module.randomId,
    val onSelectionChanged: (selectedItems: Set<T>) -> Unit = {}
) : ExpandableModule<MultipleSelectionListModule<T>>, ValueWrapperModule<Set<String>, MultipleSelectionListModule<T>> {

    override val onValueChanged: (newValue: Set<String>) -> Unit = { newValue -> onSelectionChanged(newValue.toItems()) }
    override val initialValue: Set<String> = initiallySelectedItemIds
    override val text: (Set<String>) -> Text = { title(it.toItems()) }

    override fun getHeaderTitle(beagle: BeagleContract) = title(getCurrentValue(beagle).toItems())

    private fun Set<String>?.toItems() = orEmpty().mapNotNull { itemId -> items.firstOrNull { it.id == itemId } }.toSet()

    companion object {
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private const val DEFAULT_SHOULD_REQUIRE_CONFIRMATION = false
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}