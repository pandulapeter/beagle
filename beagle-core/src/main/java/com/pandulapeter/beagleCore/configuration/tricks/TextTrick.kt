package com.pandulapeter.beagleCore.configuration.tricks

import java.util.UUID

/**
 * Displays simple text content.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
 * @param text - The text that should be displayed.
 * @param isTitle - Whether or not the text should appear in bold style. False by default.
 */
data class TextTrick(
    override val id: String = UUID.randomUUID().toString(),
    val text: CharSequence,
    val isTitle: Boolean = false
) : Trick

@Suppress("unused")
typealias TextModule = TextTrick