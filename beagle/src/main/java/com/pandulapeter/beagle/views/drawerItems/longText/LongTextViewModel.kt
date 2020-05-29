package com.pandulapeter.beagle.views.drawerItems.longText

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class LongTextViewModel(
    override val id: String,
    val text: CharSequence
) : DrawerItemViewModel