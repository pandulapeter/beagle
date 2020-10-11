package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
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
    val subtitle: Text? = DEFAULT_SUBTITLE,
    val text: Text? = DEFAULT_TEXT
) : Module<HeaderModule> {

    override val id: String = ID

    companion object {
        const val ID = "header"
        private val DEFAULT_SUBTITLE: Text? = null
        private val DEFAULT_TEXT: Text? = null
    }
}