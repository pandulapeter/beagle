package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import java.util.UUID

/**
 * Displays a longer piece of text that can be collapsed into a title.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param isExpandedInitially - Whether or not the text is expanded the first time the module becomes visible. Optional, false by default.
 * @param text - The text to display.
 */
data class LongTextModule(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    override val isExpandedInitially: Boolean = false,
    val text: CharSequence
) : ExpandableModule<LongTextModule> {

    override val canExpand = text.isNotBlank()

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}