package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import java.util.UUID

/**
 * Displays a list of check boxes represented by [BeagleListItemContract] instances. Any number of items can be selected at any given time.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param items - The list of items that should be displayed.
 * @param initiallySelectedItemIds - The ID-s of the items that should be selected initially. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isValuePersisted - Can be used to enable or disable persisting the selected value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param onSelectionChanged - Callback called when the changes the selection. The parameter is a set with the currently selected items.
 */
data class MultipleSelectionListModule<T : BeagleListItemContract>(
    override val title: CharSequence,
    val items: List<T>,
    val initiallySelectedItemIds: Set<String>,
    override val isValuePersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val isExpandedInitially: Boolean = false,
    override val id: String = UUID.randomUUID().toString(),
    val onSelectionChanged: (selectedItems: Set<T>) -> Unit
) : ExpandableModule<MultipleSelectionListModule<T>>, ValueWrapperModule<Set<String>, MultipleSelectionListModule<T>> {

    override val onValueChanged: (newValue: Set<String>) -> Unit = { newValue -> onSelectionChanged(newValue.mapNotNull { itemId -> items.firstOrNull { it.id == itemId } }.toSet()) }
    override val initialValue: Set<String> = initiallySelectedItemIds

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}