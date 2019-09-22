package com.pandulapeter.debugMenu.views.items

internal interface DrawerItemViewModel {

    val id: String
    val shouldUsePayloads: Boolean get() = false
}