package com.pandulapeter.debugMenu.views.items.settingsLink

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.modules.SettingsLinkModule

internal data class SettingsLinkViewModel(override val textColor: Int, private val settingsLinkModule: SettingsLinkModule) : DrawerItem {

    override val id = "SettingsLink"
    val text = settingsLinkModule.text
}