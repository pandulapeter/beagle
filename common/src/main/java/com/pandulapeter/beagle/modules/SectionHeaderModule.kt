package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.commonBase.randomId

/**
 * Displays a section header title.
 *
 * @param title - The text to display on the label.
 * @param id - A unique identifier for the module. [randomId] by default.
 */
@Suppress("unused")
@Deprecated("Use a TextModule with the TextModule.Type.SECTION_HEADER type instead - this class will be removed.")
data class SectionHeaderModule(
    val title: Text,
    override val id: String = randomId
) : Module<SectionHeaderModule> {

    constructor(
        title: CharSequence,
        id: String = randomId
    ) : this(
        title = title.toText(),
        id = id
    )

    constructor(
        @StringRes title: Int,
        id: String = randomId
    ) : this(
        title = title.toText(),
        id = id
    )
}