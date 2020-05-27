package com.pandulapeter.shared.configuration

/**
 * Specifies the behavior customization options for the debug menu. All parameters are optional.
 *
 * @param isShakeToOpenEnabled - Whether or not the menu can be opened by shaking the device. True by default.
 */
data class Behavior(
    val isShakeToOpenEnabled: Boolean = true
)