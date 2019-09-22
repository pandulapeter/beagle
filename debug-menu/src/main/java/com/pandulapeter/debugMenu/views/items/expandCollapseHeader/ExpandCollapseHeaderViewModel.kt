package com.pandulapeter.debugMenu.views.items.expandCollapseHeader

import com.pandulapeter.debugMenu.views.items.DrawerItem

internal data class ExpandCollapseHeaderViewModel(
    override val id: String,
    val title: String,
    val isExpanded: Boolean,
    val shouldShowIcon: Boolean = true
) : DrawerItem {

    override val shouldUsePayloads = true
}