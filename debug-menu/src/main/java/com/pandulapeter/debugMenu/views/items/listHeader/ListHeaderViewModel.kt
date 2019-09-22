package com.pandulapeter.debugMenu.views.items.listHeader

import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel

internal data class ListHeaderViewModel(
    override val id: String,
    val title: String,
    val isExpanded: Boolean,
    val shouldShowIcon: Boolean = true
) : DrawerItemViewModel {

    override val shouldUsePayloads = true
}