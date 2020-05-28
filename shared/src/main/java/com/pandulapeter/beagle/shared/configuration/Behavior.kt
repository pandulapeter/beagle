package com.pandulapeter.beagle.shared.configuration

/**
 * Specifies the behavior customization options for the debug menu. All parameters are optional.
 *
 * @param shakeThreshold - The threshold value above which the debug menu will be shown when the user shakes the device. Set to null to disable shake detection. 1200 by default.
 */
data class Behavior(
    val shakeThreshold: Int? = 1200
)