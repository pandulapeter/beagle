package com.pandulapeter.debugMenuCore.configuration

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import kotlinx.android.parcel.Parcelize

/**
 * Specifies the UI customization options for the debug drawer. All parameters are optional.
 * @param backgroundColor - The resolved background color for the drawer. If null, the default window background will be used.
 * @param textColor - The resolved text color to be used for all texts. If null, textColorPrimary will be used.
 * @param drawerWidth - Custom width for the drawer. If null, 280dp will be used.
 */
@Parcelize
data class UiConfiguration(
    @ColorInt val backgroundColor: Int? = null,
    @ColorInt val textColor: Int? = null,
    @Dimension val drawerWidth: Int? = null
) : Parcelable