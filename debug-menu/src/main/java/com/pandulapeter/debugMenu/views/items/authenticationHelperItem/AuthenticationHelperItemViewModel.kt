package com.pandulapeter.debugMenu.views.items.authenticationHelperItem

import com.pandulapeter.debugMenu.views.items.DrawerItem

internal data class AuthenticationHelperItemViewModel(val item: Pair<String, String>) : DrawerItem {

    override val id = "authenticationHelperItem_${item.first}"
}