package com.pandulapeter.beagleCore.configuration.modules

import com.pandulapeter.beagleCore.contracts.BeagleModuleContract

/**
 * Displays a header on top of the drawer with general information about the app / build.
 * This module can only be added once and will always appear on top.
 *
 * @param title - The title of the app / the debug menu. Null by default (hidden title).
 * @param subtitle - The subtitle of the the debug menu. Consider using the build version ("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"). Null by default (hidden subtitle).
 * @param text - Additional text you want to display on the debug drawer. Null by default (hidden text).
 */
data class HeaderModule(
    val title: CharSequence? = null,
    val subtitle: CharSequence? = null,
    val text: CharSequence? = null
) : BeagleModuleContract {

    override val id = ID

    companion object {
        const val ID = "header"
    }
}