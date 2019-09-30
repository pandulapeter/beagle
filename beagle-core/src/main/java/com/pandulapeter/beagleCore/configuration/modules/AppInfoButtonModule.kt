package com.pandulapeter.beagleCore.configuration.modules

import com.pandulapeter.beagleCore.contracts.BeagleModuleContract

/**
 * Displays a button that links to the Android App Info page for your app.
 * This module can only be added once.
 *
 * @param text - The text that should be displayed on the button. "Show app info" by default.
 */
data class AppInfoButtonModule(
    val text: CharSequence = "Show app info"
) : BeagleModuleContract {

    override val id = ID

    companion object {
        const val ID = "appInfoButton"
    }
}