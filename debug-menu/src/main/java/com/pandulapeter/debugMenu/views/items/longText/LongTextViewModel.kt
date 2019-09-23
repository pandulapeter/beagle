package com.pandulapeter.debugMenu.views.items.longText

import com.pandulapeter.debugMenu.views.items.DrawerItemViewModel

internal data class LongTextViewModel(
    override val id: String,
    val text: CharSequence
) : DrawerItemViewModel