package com.pandulapeter.debugMenu.views.items.header

import com.pandulapeter.debugMenu.BuildConfig
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.HeaderModule

internal data class HeaderViewModel(private val headerModule: HeaderModule) : DrawerItem {

    override val id = "header"
    val title = headerModule.title
    val subtitle = headerModule.subtitle
    val buildTime = if (headerModule.shouldShowBuildDate) {
        if (headerModule.shouldShowBuildTime) "${BuildConfig.BUILD_DATE}, ${BuildConfig.BUILD_TIME}" else BuildConfig.BUILD_DATE
    } else {
        if (headerModule.shouldShowBuildTime) BuildConfig.BUILD_TIME else null
    }
    val text = headerModule.text
}