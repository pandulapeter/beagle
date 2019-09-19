package com.pandulapeter.debugMenu.views.items.settingsLink

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.SettingsLinkModule

internal data class SettingsLinkViewModel(private val settingsLinkModule: SettingsLinkModule) : DrawerItem {

    override val id = "settingsLink"
    val text = settingsLinkModule.text
}