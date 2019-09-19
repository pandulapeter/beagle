package com.pandulapeter.debugMenu.views.items.authenticationHelperHeader

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.AuthenticationHelperModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule

internal data class AuthenticationHelperHeaderViewModel(private val authenticationHelperModule: AuthenticationHelperModule, val isExpanded: Boolean) : DrawerItem {

    override val id = "authenticationHelperHeader"
    val title = authenticationHelperModule.title
}