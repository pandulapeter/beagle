package com.pandulapeter.beagle.views.items.button

import com.pandulapeter.beagle.views.items.DrawerItemViewModel

internal data class ButtonViewModel(
    override val id: String,
    val shouldUseListItem: Boolean,
    val text: CharSequence,
    val onButtonPressed: () -> Unit
) : DrawerItemViewModel