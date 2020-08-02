package com.pandulapeter.beagle.common.listeners

import android.graphics.Canvas
import com.pandulapeter.beagle.common.configuration.Insets

/**
 * Implement this interface to get notified about when to draw over the application's layout.
 * You can manually trigger the [onDrawOver] function by calling Beagle.invalidateOverlay().
 *
 * This could be useful when writing custom modules.
 */
interface OverlayListener {

    /**
     * Called after the root layout is redrawn.
     *
     * @param canvas - The [Canvas] you can use to draw over the View hierarchy.
     * @param insets - The system window inset for the current decorView, (all attributes will have the value of 0 on SDK levels where this is not available).
     */
    fun onDrawOver(canvas: Canvas, insets: Insets)
}