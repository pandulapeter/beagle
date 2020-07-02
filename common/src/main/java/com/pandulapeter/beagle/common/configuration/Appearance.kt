package com.pandulapeter.beagle.common.configuration

import androidx.annotation.StyleRes

/**
 * Specifies the appearance customization options for the debug menu. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID the menu should use. If null, each menu will take their Activity's theme. Null by default.
 * @param applyButtonText - The text on the Apply button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Apply" by default.
 * @param resetButtonText - The text on the Reset button that appears when the user makes changes that are not handled in real-time (see the "shouldRequireConfirmation" parameter of some Modules). "Reset" by default.
 */
data class Appearance(
    @StyleRes val themeResourceId: Int? = null,
    val applyButtonText: CharSequence = "Apply",
    val resetButtonText: CharSequence = "Reset"
)