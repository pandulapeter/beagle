package com.pandulapeter.debugMenuCore.modules

data class HeaderModule(
    val title: String? = null,
    val subtitle: String? = null,
    val shouldShowBuildTime: Boolean = false
) : DebugMenuModule