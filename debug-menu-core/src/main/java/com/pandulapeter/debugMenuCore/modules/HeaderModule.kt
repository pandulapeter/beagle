package com.pandulapeter.debugMenuCore.modules

data class HeaderModule(
    val title: String? = null,
    val subtitle: String? = null,
    val shouldShowBuildDate: Boolean = true,
    val shouldShowBuildTime: Boolean = true
) : DebugMenuModule