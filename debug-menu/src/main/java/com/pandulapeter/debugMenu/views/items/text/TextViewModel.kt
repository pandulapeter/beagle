package com.pandulapeter.debugMenu.views.items.text

import com.pandulapeter.debugMenu.views.items.DrawerItem

internal data class TextViewModel(
    override val id: String,
    val text: CharSequence
) : DrawerItem