package com.pandulapeter.debugMenu.views.items.button

import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel

internal data class ButtonViewModel(
    override val id: String,
    val text: CharSequence,
    val onButtonPressed: () -> Unit
) : DrawerItemViewModel