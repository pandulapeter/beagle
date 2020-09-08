package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AppInfoButtonModule.Companion.ID
import com.pandulapeter.beagle.modules.HeaderModule.Companion.ID


/**
 * Displays a configurable title, subtitle and text.
 *
 * This module can only be added once. It uses the value of [ID] as id.
 *
 * @param title - The title that will be displayed with the largest font.
 * @param subtitle - The subtitle appears below the title. Optional, null by default.
 * @param text - The text appears below the subtitle. Optional, null by default.
 */
data class HeaderModule(
    val title: CharSequence,
    val subtitle: CharSequence? = null,
    val text: CharSequence? = null
) : Module<HeaderModule> {

    override val id: String = ID

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")

    companion object {
        const val ID = "header"
    }
}