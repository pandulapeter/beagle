package com.pandulapeter.beagle.modules

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.modules.TextModule.Companion.DEFAULT_ICON
import com.pandulapeter.beagle.modules.TextModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.TextModule.Companion.DEFAULT_ON_ITEM_SELECTED
import com.pandulapeter.beagle.modules.TextModule.Companion.DEFAULT_TYPE
import com.pandulapeter.beagle.modules.TextModule.Type

/**
 * Displays a piece of text. Can be used for click handling as well.
 *
 * @param text - The text to display.
 * @param type - Specify a [Type] to apply a specific appearance. [DEFAULT_TYPE] by default.
 * @param icon - A drawable resource ID that will be tinted and displayed before the text, or null to display no icon. [DEFAULT_ICON] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param id - A unique identifier for the module. [randomId] by default.
 * @param onItemSelected - Callback invoked when the user clicks on the text, or null to disable selection. [DEFAULT_ON_ITEM_SELECTED] by default.
 */
@Suppress("unused")
data class TextModule(
    val text: Text,
    val type: Type = DEFAULT_TYPE,
    @DrawableRes val icon: Int? = DEFAULT_ICON,
    val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val id: String = randomId,
    val onItemSelected: (() -> Unit)? = DEFAULT_ON_ITEM_SELECTED
) : Module<TextModule> {

    constructor(
        text: CharSequence,
        type: Type = DEFAULT_TYPE,
        @DrawableRes icon: Int? = DEFAULT_ICON,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        id: String = randomId,
        onItemSelected: (() -> Unit)? = DEFAULT_ON_ITEM_SELECTED
    ) : this(
        text = Text.CharSequence(text),
        type = type,
        icon = icon,
        isEnabled = isEnabled,
        id = id,
        onItemSelected = onItemSelected
    )

    constructor(
        @StringRes text: Int,
        type: Type = DEFAULT_TYPE,
        @DrawableRes icon: Int? = DEFAULT_ICON,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        id: String = randomId,
        onItemSelected: (() -> Unit)? = DEFAULT_ON_ITEM_SELECTED
    ) : this(
        text = Text.ResourceId(text),
        type = type,
        icon = icon,
        isEnabled = isEnabled,
        id = id,
        onItemSelected = onItemSelected
    )

    enum class Type {
        NORMAL,
        SECTION_HEADER,
        BUTTON
    }

    companion object {
        private val DEFAULT_ON_ITEM_SELECTED: (() -> Unit)? = null
        private val DEFAULT_TYPE = Type.NORMAL
        private val DEFAULT_ICON: Int? = null
        private const val DEFAULT_IS_ENABLED = true
    }
}