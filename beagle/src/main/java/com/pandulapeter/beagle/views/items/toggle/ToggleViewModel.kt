package com.pandulapeter.beagle.views.items.toggle

import com.pandulapeter.beagle.views.items.DrawerItemViewModel

internal data class ToggleViewModel(
    override val id: String,
    val title: CharSequence,
    val isEnabled: Boolean,
    val onToggleStateChanged: (newValue: Boolean) -> Unit
) : DrawerItemViewModel {

    override val shouldUsePayloads = true
}