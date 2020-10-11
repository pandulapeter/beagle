package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.LongTextModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY

/**
 * Displays a longer piece of text that can be collapsed into a title.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param text - The text to display.
 * @param isExpandedInitially - Whether or not the text is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 */
data class LongTextModule(
    val title: Text,
    val text: Text,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = Module.randomId
) : ExpandableModule<LongTextModule> {

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}