package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * Displays a section header title.
 *
 * @param title - The text to display on the label.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 */
data class SectionHeaderModule(
    val title: Text,
    override val id: String = Module.randomId
) : Module<SectionHeaderModule>