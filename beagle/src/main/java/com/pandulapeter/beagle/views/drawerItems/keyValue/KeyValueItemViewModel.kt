package com.pandulapeter.beagle.views.drawerItems.keyValue

import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class KeyValueItemViewModel(
    private val trickId: String,
    private val pair: Pair<CharSequence, CharSequence>
) : DrawerItemViewModel {

    override val id = "keyValue_${trickId}_${pair.first}"
    val key = pair.first
    val value = pair.second
}