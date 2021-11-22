package com.pandulapeter.beagle.common.configuration

import androidx.core.view.WindowInsetsCompat

/**
 * Data class representing system window insets.
 */
data class Insets(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0
)

fun WindowInsetsCompat.getBeagleInsets(typeMask: Int): Insets =
    getInsets(typeMask).mapToBeagleInsets()

private fun androidx.core.graphics.Insets.mapToBeagleInsets(): Insets =
    Insets(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )