package com.pandulapeter.beagle.views.items.longText

import com.pandulapeter.beagle.views.items.DrawerItemViewModel

internal data class LongTextViewModel(
    override val id: String,
    val text: CharSequence
) : DrawerItemViewModel