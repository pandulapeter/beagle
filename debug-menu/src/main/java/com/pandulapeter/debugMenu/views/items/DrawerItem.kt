package com.pandulapeter.debugMenu.views.items

internal interface DrawerItem {

    val id: String
    val shouldUsePayloads: Boolean get() = false
}