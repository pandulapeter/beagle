package com.pandulapeter.beagle.common.listeners

import android.graphics.Canvas

/**
 * Implement this interface to get notified about when to draw over the application's layout.
 * You can manually trigger the [drawOver] function by calling Beagle.invalidateOverlay().
 *
 * This could be useful when writing custom modules.
 */
interface OverdrawListener {

    /**
     * Called after the root layout is redrawn.
     * TODO: Expose information about the window insets.
     *
     * @param canvas - The [Canvas] you can use to draw over the View hierarchy.
     */
    fun drawOver(canvas: Canvas)
}