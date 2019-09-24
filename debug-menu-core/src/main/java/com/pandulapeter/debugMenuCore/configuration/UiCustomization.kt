package com.pandulapeter.debugMenuCore.configuration

import android.os.Parcelable
import androidx.annotation.Dimension
import androidx.annotation.StyleRes
import kotlinx.android.parcel.Parcelize

/**
 * Specifies the UI customization options for the debug drawer. All parameters are optional.
 *
 * @param themeResourceId - The theme resource ID the drawers should use. If null, each drawer will take their Activity's theme. Null by default.
 * @param drawerWidth - Custom width for the drawer. If null, 280dp will be used. Null by default.
 */
//TODO: Button color, button text color, ripple color.
@Parcelize
data class UiCustomization(
    @StyleRes val themeResourceId: Int? = null,
    @Dimension val drawerWidth: Int? = null
) : Parcelable