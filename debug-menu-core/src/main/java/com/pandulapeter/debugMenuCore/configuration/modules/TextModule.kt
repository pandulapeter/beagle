package com.pandulapeter.debugMenuCore.configuration.modules

import java.util.UUID

/**
 * Displays a simple text content.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
 * @param text - The text that should be displayed.
 */
data class TextModule(
    override val id: String = UUID.randomUUID().toString(),
    val text: CharSequence
) : DebugMenuModule