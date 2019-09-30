package com.pandulapeter.beagleCore.configuration.tricks

import androidx.annotation.ColorInt
import androidx.annotation.Dimension

/**
 * Displays a switch that, when enabled, draws a grid over your app with configurable dimensions that you can use to check the alignments of your Views.
 * This module can only be added once.
 *
 * @param title - The text that appears near the switch. "Keyline overlay" by default.
 * @param keylineGrid - The distance between the grid lines. 8dp by default.
 * @param keylinePrimary - The distance between the edge of the screen and the primary keyline. 16dp by default (24dp on tablets).
 * @param keylinePrimary - The distance between the edge of the screen and the secondary keyline. 72dp by default (80dp on tablets).
 * @param gridColor - The color to be used when drawing the grid. By default it will be the debug menu's text color.
 */
data class KeylineOverlayToggleTrick(
    val title: CharSequence = "Keyline overlay",
    @Dimension val keylineGrid: Int? = null,
    @Dimension val keylinePrimary: Int? = null,
    @Dimension val keylineSecondary: Int? = null,
    @ColorInt val gridColor: Int? = null
) : Trick {

    override val id = ID

    companion object {
        const val ID = "keylineOverlay"
    }
}

@Suppress("unused")
typealias KeylineOverlayToggleModule = KeylineOverlayToggleTrick