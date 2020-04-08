package com.pandulapeter.beagle.views.drawerItems.listHeader

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class ListHeaderViewModel(
    override val id: String,
    val title: CharSequence,
    val isExpanded: Boolean,
    val shouldShowIcon: Boolean = true,
    val onItemSelected: () -> Unit
) : DrawerItemViewModel