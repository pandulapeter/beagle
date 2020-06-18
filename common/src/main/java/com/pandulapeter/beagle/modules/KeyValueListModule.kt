package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import java.util.UUID

/**
 * Displays a list of key-value pairs that can be collapsed into a header.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param pairs - The list of key-value pairs to display.
 */
data class KeyValueListModule(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    override val isExpandedInitially: Boolean = false,
    val pairs: List<Pair<CharSequence, CharSequence>>
) : ExpandableModule<KeyValueListModule> {

    override val canExpand = pairs.isNotEmpty()

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}