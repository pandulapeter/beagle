package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import java.util.UUID

/**
 * Displays a list of key-value pairs that can be collapsed into a header.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param pairs - The list of key-value pairs to display.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param id - A unique identifier for the module. Optional, random string by default.
 */
data class KeyValueListModule(
    val title: Text,
    val pairs: List<Pair<CharSequence, CharSequence>>,
    override val isExpandedInitially: Boolean = false,
    override val id: String = UUID.randomUUID().toString()
) : ExpandableModule<KeyValueListModule> {

    override fun getInternalTitle(beagle: BeagleContract) = title

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}