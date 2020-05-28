package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StyleRes

/**
 * Specifies the appearance customization options for the debug menu. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID the menu should use. If null, each menu will take their Activity's theme. Null by default.
 */
data class Appearance(
    @StyleRes val themeResourceId: Int? = null
)