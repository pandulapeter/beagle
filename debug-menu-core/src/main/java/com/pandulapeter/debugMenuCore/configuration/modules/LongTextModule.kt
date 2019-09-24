package com.pandulapeter.debugMenuCore.configuration.modules

import java.util.UUID

/**
 * Displays a longer piece of text that can be collapsed into a title.
 * This module can be added multiple times as long as the ID is unique.
 *
 * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
 * @param title - The title of the module.
 * @param text - The text that should be displayed.
 * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
 */
data class LongTextModule(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    val text: CharSequence,
    override val isInitiallyExpanded: Boolean = false
) : DebugMenuExpandableModule