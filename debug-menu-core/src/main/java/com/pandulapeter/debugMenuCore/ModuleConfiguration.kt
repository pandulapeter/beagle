package com.pandulapeter.debugMenuCore

import com.pandulapeter.debugMenuCore.modules.HeaderModule
import com.pandulapeter.debugMenuCore.modules.LoggingModule
import com.pandulapeter.debugMenuCore.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.modules.SettingsLinkModule

/**
 * Specifies the module configuration for the debug drawer. All parameters are optional.
 * @param headerModule - Allows to add a [HeaderModule] to the drawer.
 * @param settingsLinkModule - Allows to add a [SettingsLinkModule] to the drawer.
 * @param networkLoggingModule - Allows to add a [NetworkLoggingModule] to the drawer.
 * @param loggingModule - Allows to add a [LoggingModule] to the drawer.
 */
data class ModuleConfiguration(
    val headerModule: HeaderModule? = null,
    val settingsLinkModule: SettingsLinkModule? = null,
    val networkLoggingModule: NetworkLoggingModule? = null,
    val loggingModule: LoggingModule? = null
)