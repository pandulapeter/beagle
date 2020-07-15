package com.pandulapeter.beagle.common.listeners

import android.graphics.Canvas

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
     * @param leftInset - The left system window inset for the current decorView, or 0 on SDK levels where this is not available.
     * @param topInset - The top system window inset for the current decorView, or 0 on SDK levels where this is not available.
     * @param rightInset - The right system window inset for the current decorView, or 0 on SDK levels where this is not available.
     * @param bottomInset - The bottom system window inset for the current decorView, or 0 on SDK levels where this is not available.
     */
    fun onDrawOver(canvas: Canvas, leftInset: Int, topInset: Int, rightInset: Int, bottomInset: Int)
}