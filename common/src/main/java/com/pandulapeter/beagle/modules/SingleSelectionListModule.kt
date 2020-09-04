package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import java.util.UUID

/**
 * Displays a list of radio buttons represented by [BeagleListItemContract] instances. Only one item is selected at any given time.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param items - The list of items that should be displayed.
 * @param initiallySelectedItemId - The ID of the item that should be selected initially, or null for no initial selection. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. Optional, true by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the selected value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. Optional, false by default.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param onSelectionChanged - Callback called when the changes the selection. The parameter is the currently selected item. Empty implementation by default.
 */
data class SingleSelectionListModule<T : BeagleListItemContract>(
    override val title: CharSequence,
    val items: List<T>,
    val initiallySelectedItemId: String?,
    override val isEnabled: Boolean = true,
    override val isValuePersisted: Boolean = false,
    override val shouldRequireConfirmation: Boolean = false,
    override val isExpandedInitially: Boolean = false,
    override val id: String = UUID.randomUUID().toString(),
    val onSelectionChanged: (selectedItem: T?) -> Unit = {}
) : ExpandableModule<SingleSelectionListModule<T>>, ValueWrapperModule<String, SingleSelectionListModule<T>> {

    override val initialValue = initiallySelectedItemId.orEmpty() //TODO: Using an empty string is not a great idea, this should be null
    override val onValueChanged: (newValue: String) -> Unit = { newValue -> onSelectionChanged(items.firstOrNull { it.id == newValue }) }

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}