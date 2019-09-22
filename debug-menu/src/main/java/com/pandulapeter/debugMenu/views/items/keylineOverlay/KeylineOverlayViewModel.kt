package com.pandulapeter.debugMenu.views.items.keylineOverlay

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayModule

internal data class KeylineOverlayViewModel(val keylineOverlayModule: KeylineOverlayModule, val isEnabled: Boolean) : DrawerItem {

    override val id = "keylineOverlay"
    override val shouldUsePayloads = true
    val title = keylineOverlayModule.title
}