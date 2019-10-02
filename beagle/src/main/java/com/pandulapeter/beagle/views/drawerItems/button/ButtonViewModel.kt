package com.pandulapeter.beagle.views.drawerItems.button

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class ButtonViewModel(
    override val id: String,
    val shouldUseListItem: Boolean,
    val text: CharSequence,
    val onButtonPressed: () -> Unit
) : DrawerItemViewModel