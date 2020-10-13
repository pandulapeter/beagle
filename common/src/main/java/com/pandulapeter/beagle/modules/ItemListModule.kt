package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.modules.ItemListModule.Companion.DEFAULT_IN_ITEM_SELECTED
import com.pandulapeter.beagle.modules.ItemListModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY

/**
 * Displays a list of simple items represented by [BeagleListItemContract].
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param items - The list of items that should be displayed.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 * @param id - A unique identifier for the module. [randomId] by default.
 * @param onItemSelected - Callback invoked when the user selects one of the items, or null to disable selection. The parameter of the lambda function is the selected item. [DEFAULT_IN_ITEM_SELECTED] by default.
 */
@Suppress("unused")
data class ItemListModule<T : BeagleListItemContract>(
    val title: Text,
    val items: List<T>,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = randomId,
    val onItemSelected: ((item: T) -> Unit)? = DEFAULT_IN_ITEM_SELECTED
) : ExpandableModule<ItemListModule<T>> {

    constructor(
        title: CharSequence,
        items: List<T>,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = randomId,
        onItemSelected: ((item: T) -> Unit)? = DEFAULT_IN_ITEM_SELECTED
    ) : this(
        title = Text.CharSequence(title),
        items = items,
        isExpandedInitially = isExpandedInitially,
        id = id,
        onItemSelected = onItemSelected
    )

    constructor(
        @StringRes title: Int,
        items: List<T>,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = randomId,
        onItemSelected: ((item: T) -> Unit)? = DEFAULT_IN_ITEM_SELECTED
    ) : this(
        title = Text.ResourceId(title),
        items = items,
        isExpandedInitially = isExpandedInitially,
        id = id,
        onItemSelected = onItemSelected
    )

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
        private val DEFAULT_IN_ITEM_SELECTED = null
    }
}