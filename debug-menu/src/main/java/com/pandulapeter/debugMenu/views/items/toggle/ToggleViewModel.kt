package com.pandulapeter.debugMenu.views.items.toggle

import com.pandulapeter.debugMenu.views.items.DrawerItem

internal data class ToggleViewModel(
    override val id: String,
    val title: String,
    val isEnabled: Boolean,
    val onToggleStateChanged: (newValue: Boolean) -> Unit
) : DrawerItem {

    override val shouldUsePayloads = true
}