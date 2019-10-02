package com.pandulapeter.beagle.views.drawerItems

internal interface DrawerItemViewModel {

    val id: String
    val shouldUsePayloads: Boolean get() = false
}