package com.pandulapeter.beagle.modules

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.ColorInt
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.builtIn.AppInfoButtonModuleContract

/**
 * Displays a button that links to the Android App Info page for your app.
 * This module can only be added once. It uses the value of [AppInfoButtonModuleContract.ID] as id.
 *
 * @param text - The text that should be displayed on the button. Optional, "Show app info" by default.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 * @param shouldOpenInNewTask - Whether or not the App Info page will be opened with the Intent.FLAG_ACTIVITY_NEW_TASK flag. False by default.
 */
class AppInfoButtonModule(
    override val text: CharSequence = "Show app info",
    @ColorInt override val color: Int? = null,
    override val shouldOpenInNewTask: Boolean = false
) : AppInfoButtonModuleContract, ButtonModule(
    id = AppInfoButtonModuleContract.ID,
    text = text,
    color = color,
    onButtonPressed = {
        BeagleCore.implementation.hide()
        BeagleCore.implementation.currentActivity?.run {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
                if (shouldOpenInNewTask) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }
)