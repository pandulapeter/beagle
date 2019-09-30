package com.pandulapeter.beagle.views.items.text

import com.pandulapeter.beagle.views.items.DrawerItemViewModel

internal data class TextViewModel(
    override val id: String,
    val text: CharSequence,
    val isTitle: Boolean
) : DrawerItemViewModel