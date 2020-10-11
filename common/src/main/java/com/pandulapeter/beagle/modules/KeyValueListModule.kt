package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.KeyValueListModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY

/**
 * Displays a list of key-value pairs that can be collapsed into a header.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param pairs - The list of key-value pairs to display.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 */
data class KeyValueListModule(
    val title: Text,
    val pairs: List<Pair<CharSequence, CharSequence>>,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = Module.randomId
) : ExpandableModule<KeyValueListModule> {

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}