package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.modules.LongTextModule.Companion.DEFAULT_IS_EXPANDED_INITIALLY

/**
 * Displays a longer piece of text that can be collapsed into a title.
 *
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param text - The text to display.
 * @param isExpandedInitially - Whether or not the text is expanded the first time the module becomes visible. [DEFAULT_IS_EXPANDED_INITIALLY] by default.
 * @param id - A unique identifier for the module. [BeagleContract.randomId] by default.
 */
@Suppress("unused")
data class LongTextModule(
    val title: Text,
    val text: Text,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = BeagleContract.randomId
) : ExpandableModule<LongTextModule> {

    constructor(
        title: CharSequence,
        text: CharSequence,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = BeagleContract.randomId
    ) : this(
        title = Text.CharSequence(title),
        text = Text.CharSequence(text),
        isExpandedInitially = isExpandedInitially,
        id = id
    )

    constructor(
        @StringRes title: Int,
        @StringRes text: Int,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = BeagleContract.randomId
    ) : this(
        title = Text.ResourceId(title),
        text = Text.ResourceId(text),
        isExpandedInitially = isExpandedInitially,
        id = id
    )

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}