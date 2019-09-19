package com.pandulapeter.debugMenuCore.configuration

import com.pandulapeter.debugMenuCore.configuration.modules.AuthenticationHelperModule
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule
import com.pandulapeter.debugMenuCore.configuration.modules.KeylineOverlayModule
import com.pandulapeter.debugMenuCore.configuration.modules.LoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.NetworkLoggingModule
import com.pandulapeter.debugMenuCore.configuration.modules.SettingsLinkModule

/**
 * Specifies the module configuration for the debug drawer. All parameters are optional.
 * @param headerModule - Allows to add a [HeaderModule] to the drawer.
 * @param settingsLinkModule - Allows to add a [SettingsLinkModule] to the drawer.
 * @param keylineOverlayModule - Allows to add a [KeylineOverlayModule] to the drawer.
 * @param authenticationHelperModule - Allows to add a [AuthenticationHelperModule] to the drawer.
 * @param networkLoggingModule - Allows to add a [NetworkLoggingModule] to the drawer.
 * @param loggingModule - Allows to add a [LoggingModule] to the drawer.
 */
data class ModuleConfiguration(
    val headerModule: HeaderModule? = null,
    val settingsLinkModule: SettingsLinkModule? = null,
    val keylineOverlayModule: KeylineOverlayModule? = null,
    val authenticationHelperModule: AuthenticationHelperModule? = null,
    val networkLoggingModule: NetworkLoggingModule? = null,
    val loggingModule: LoggingModule? = null
)