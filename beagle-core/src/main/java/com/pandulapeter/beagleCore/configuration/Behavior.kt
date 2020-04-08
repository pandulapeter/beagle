package com.pandulapeter.beagleCore.configuration

import android.app.Activity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Specifies the behavior customization options for the debug drawer. All parameters are optional.
 *
 * @param triggerGesture - Specifies the way the drawer can be opened. [TriggerGesture.SWIPE_AND_SHAKE] by default.
 * @param shouldShowResetButton - Whether or not to display a Reset button besides the Apply button that appears when the user makes changes that are not handled in real-time (see the "needsConfirmation" parameter of some Tricks). True by default.
 * @param shouldResetOnClose - Whether or not to reset all pending changes when the drawer is closed (see the "needsConfirmation" parameter of some Tricks). False by default.
 * @param packageName - Tha base package name of the application. Beagle will only work in Activities that are under this package. If not specified, an educated guess will be made (that won't work if your setup includes product flavors for example).
 * @param excludedActivities - The list of Activity classes where you specifically don't want to use Beagle. Empty by default.
 */
@Parcelize
data class Behavior(
    val triggerGesture: TriggerGesture = TriggerGesture.SWIPE_AND_SHAKE,
    val shouldShowResetButton: Boolean = true,
    val shouldResetOnClose: Boolean = false,
    val packageName: String? = null,
    val excludedActivities: List<Class<out Activity>> = emptyList()
) : Parcelable