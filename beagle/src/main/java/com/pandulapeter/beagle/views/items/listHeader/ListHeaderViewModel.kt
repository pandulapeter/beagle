package com.pandulapeter.beagle.views.items.listHeader

import com.pandulapeter.beagle.views.items.DrawerItemViewModel

internal data class ListHeaderViewModel(
    override val id: String,
    val title: CharSequence,
    val isExpanded: Boolean,
    val shouldShowIcon: Boolean = true,
    val onItemSelected: () -> Unit
) : DrawerItemViewModel {

    override val shouldUsePayloads = true
}