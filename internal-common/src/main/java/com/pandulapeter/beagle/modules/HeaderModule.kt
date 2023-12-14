package com.pandulapeter.beagle.modules

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.HeaderModule.Companion.DEFAULT_SUBTITLE
import com.pandulapeter.beagle.modules.HeaderModule.Companion.DEFAULT_TEXT
import com.pandulapeter.beagle.modules.HeaderModule.Companion.ID


/**
 * Displays a configurable title, subtitle and text.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title that will be displayed with the largest font.
 * @param subtitle - The subtitle appears below the title. [DEFAULT_SUBTITLE] by default.
 * @param text - The text appears below the subtitle. [DEFAULT_TEXT] by default.
 */
data class HeaderModule(
    val title: Text,
    val subtitle: Text? = DEFAULT_SUBTITLE?.toText(),
    val text: Text? = DEFAULT_TEXT?.toText()
) : Module<HeaderModule> {

    constructor(
        title: CharSequence,
        subtitle: CharSequence? = DEFAULT_SUBTITLE,
        text: CharSequence? = DEFAULT_TEXT
    ) : this(
        title = title.toText(),
        subtitle = subtitle?.toText(),
        text = text?.toText()
    )

    constructor(
        @StringRes title: Int,
        @StringRes subtitle: Int? = DEFAULT_SUBTITLE_INT,
        @StringRes text: Int? = DEFAULT_TEXT_INT
    ) : this(
        title = title.toText(),
        subtitle = subtitle?.toText(),
        text = text?.toText()
    )

    override val id: String = ID

    companion object {
        const val ID = "header"
        private val DEFAULT_SUBTITLE: CharSequence? = null
        private val DEFAULT_SUBTITLE_INT: Int? = null
        private val DEFAULT_TEXT: CharSequence? = null
        private val DEFAULT_TEXT_INT: Int? = null
    }
}