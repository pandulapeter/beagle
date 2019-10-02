package com.pandulapeter.beagle.views.drawerItems.toggle

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class ToggleViewModel(
    override val id: String,
    val title: CharSequence,
    val isEnabled: Boolean,
    val onToggleStateChanged: (newValue: Boolean) -> Unit
) : DrawerItemViewModel {

    override val shouldUsePayloads = true
}