package com.pandulapeter.debugMenuCore.configuration.modules

/**
 * Displays a header on top of the drawer with general information about the build.
 * This module can only be added once and will always appear on top.
 *
 * @param title - The title of the app / the debug menu. Null by default (hidden title).
 * @param subtitle - The subtitle of the the debug menu. Consider using the build version ("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"). Null by default (hidden subtitle).
 * @param shouldShowBuildDate - If enabled, displays the day (yyyy.MM.dd) the build was created. True by default.
 * @param shouldShowBuildTime- If enabled, displays the time (HH:mm) the build was created. True by default.
 * @param text - Additional text you want to display on the debug drawer. Null by default (hidden text).
 */
data class HeaderModule(
    val title: String? = null,
    val subtitle: String? = null,
    val shouldShowBuildDate: Boolean = true,
    val shouldShowBuildTime: Boolean = true,
    val text: String? = null
) : DebugMenuModule {

    override val id = "header"
}