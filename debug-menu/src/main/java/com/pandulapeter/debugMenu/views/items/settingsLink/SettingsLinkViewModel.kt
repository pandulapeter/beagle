package com.pandulapeter.debugMenu.views.items.settingsLink

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.modules.SettingsLinkModule

//TODO: Support UI customization
internal data class SettingsLinkViewModel(private val settingsLinkModule: SettingsLinkModule) : DrawerItem {

    override val id = "SettingsLink"
    val text = settingsLinkModule.text
}