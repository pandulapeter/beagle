package com.pandulapeter.debugMenu.views.items.networkLoggingHeader

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.modules.NetworkLoggingModule

internal data class NetworkLoggingHeaderViewModel(private val networkLoggingModule: NetworkLoggingModule, val isExpanded: Boolean, val areThereLogs: Boolean) : DrawerItem {

    override val id = "networkLoggingHeader"
    val title = networkLoggingModule.title
}