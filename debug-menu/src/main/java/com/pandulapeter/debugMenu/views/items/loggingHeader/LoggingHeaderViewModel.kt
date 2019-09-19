package com.pandulapeter.debugMenu.views.items.loggingHeader

import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule

internal data class LoggingHeaderViewModel(private val loggingModule: LoggingModule, val isExpanded: Boolean, val areThereLogMessages: Boolean) : DrawerItem {

    override val id = "loggingHeader"
    val title = loggingModule.title
}