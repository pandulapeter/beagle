package com.pandulapeter.beagle.views.drawerItems.text

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class TextViewModel(
    override val id: String,
    val text: CharSequence,
    val isTitle: Boolean
) : DrawerItemViewModel