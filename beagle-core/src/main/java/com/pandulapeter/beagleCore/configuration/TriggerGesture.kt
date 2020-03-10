package com.pandulapeter.beagleCore.configuration

/**
 * Used for specifying the way the drawer can be opened.
 */
@Suppress("unused")
enum class TriggerGesture {

    /**
     * The drawer can be opened by swiping horizontally from the right (end) edge of the screen.
     */
    SWIPE_ONLY,

    /**
     * The drawer can be opened by shaking the device.
     */
    SHAKE_ONLY,

    /**
     * The drawer can be opened both by swiping horizontally from the right (end) edge of the screen and shaking the device.
     */
    SWIPE_AND_SHAKE,

    /**
     * The drawer can only be opened by calling Beagle.fetch().
     */
    NONE
}