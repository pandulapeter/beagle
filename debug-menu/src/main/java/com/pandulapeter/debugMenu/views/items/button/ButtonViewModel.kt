package com.pandulapeter.debugMenu.views.items.button

import com.pandulapeter.debugMenu.views.items.DrawerItem

internal data class ButtonViewModel(
    override val id: String,
    val text: String,
    val onButtonPressed: () -> Unit
) : DrawerItem