package com.pandulapeter.debugMenu.views.items.listHeader

import com.pandulapeter.debugMenu.views.items.DrawerItem

internal data class ListHeaderViewModel(
    override val id: String,
    val title: String,
    val isExpanded: Boolean,
    val shouldShowIcon: Boolean = true
) : DrawerItem {

    override val shouldUsePayloads = true
}