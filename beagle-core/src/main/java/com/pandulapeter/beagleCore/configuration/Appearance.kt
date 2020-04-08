package com.pandulapeter.beagleCore.configuration

import android.os.Parcelable
import androidx.annotation.Dimension
import androidx.annotation.StyleRes
import kotlinx.android.parcel.Parcelize

/**
 * Specifies the UI customization options for the debug drawer. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID the drawers should use. If null, each drawer will take their Activity's theme. Null by default.
 * @param drawerWidth - Custom width for the drawer. If null, 280dp will be used. Null by default.
 * @param applyButtonText - The text on the Apply button that appears when the user makes changes that are not handled in real-time (see the "needsConfirmation" parameter of some Tricks). "Apply" by default.
 * @param resetButtonText - The text on the Reset button that appears when the user makes changes that are not handled in real-time (see the "needsConfirmation" parameter of some Tricks). "Reset" by default.
 */
@Parcelize
data class Appearance(
    @StyleRes val themeResourceId: Int? = null,
    @Dimension val drawerWidth: Int? = null,
    val applyButtonText: String = "Apply",
    val resetButtonText: String = "Reset",
    val shouldUseItemsInsteadOfButtons: Boolean = false
) : Parcelable