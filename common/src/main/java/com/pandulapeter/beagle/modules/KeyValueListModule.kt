package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
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
    val pairs: List<Pair<Text, Text>>,
    override val isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
    override val id: String = Module.randomId
) : ExpandableModule<KeyValueListModule> {

    constructor(
        title: CharSequence,
        pairs: List<Pair<CharSequence, CharSequence>>,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = Module.randomId
    ) : this(
        title = Text.CharSequence(title),
        pairs = pairs.map { Text.CharSequence(it.first) to Text.CharSequence(it.second) },
        isExpandedInitially = isExpandedInitially,
        id = id
    )

    constructor(
        @StringRes title: Int,
        pairs: List<Pair<CharSequence, CharSequence>>,
        isExpandedInitially: Boolean = DEFAULT_IS_EXPANDED_INITIALLY,
        id: String = Module.randomId
    ) : this(
        title = Text.ResourceId(title),
        pairs = pairs.map { Text.CharSequence(it.first) to Text.CharSequence(it.second) },
        isExpandedInitially = isExpandedInitially,
        id = id
    )

    override fun getHeaderTitle(beagle: BeagleContract) = title

    companion object {
        private const val DEFAULT_IS_EXPANDED_INITIALLY = false
    }
}