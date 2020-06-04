package com.pandulapeter.beagle.common.configuration

/**
 * Specifies the behavior customization options for the debug menu. All parameters are optional.
 *
 * @param shakeThreshold - The threshold value above which the debug menu will be shown when the user shakes the device. Values between 5 - 20 work best (smaller values result in more sensitive detection). Set to null to disable shake detection. 12 by default.
 * @param excludedPackageNames - The list of packages that contain Activities for which Beagle should not be triggered. Empty by default (however the library also contains a hardcoded list).
 */
data class Behavior(
    val shakeThreshold: Int? = 12,
    val excludedPackageNames: List<String> = emptyList()
)