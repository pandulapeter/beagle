package com.pandulapeter.debugMenu.views.items.header

import com.pandulapeter.debugMenu.BuildConfig
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.modules.HeaderModule

internal data class HeaderViewModel(val textColor: Int, private val headerModule: HeaderModule) : DrawerItem {

    override val id = "Header"
    val title = headerModule.title
    val subtitle = headerModule.subtitle
    val buildTime = if (headerModule.shouldShowBuildDate) {
        if (headerModule.shouldShowBuildTime) "${BuildConfig.BUILD_DATE}, ${BuildConfig.BUILD_TIME}" else BuildConfig.BUILD_DATE
    } else {
        if (headerModule.shouldShowBuildTime) BuildConfig.BUILD_TIME else null
    }
}