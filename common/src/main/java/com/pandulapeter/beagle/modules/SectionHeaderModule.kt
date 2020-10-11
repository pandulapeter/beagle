package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module

/**
 * Displays a section header title.
 *
 * @param title - The text to display on the label.
 * @param id - A unique identifier for the module. [Module.randomId] by default.
 */
@Deprecated("Use a TextModule with the TextModule.Type.SECTION_HEADER type instead - this class will be removed.")
data class SectionHeaderModule(
    val title: Text,
    override val id: String = Module.randomId
) : Module<SectionHeaderModule> {

    constructor(
        title: CharSequence,
        id: String = Module.randomId
    ) : this(
        title = Text.CharSequence(title),
        id = id
    )

    constructor(
        @StringRes title: Int,
        id: String = Module.randomId
    ) : this(
        title = Text.ResourceId(title),
        id = id
    )
}