package com.pandulapeter.beagle.views.drawerItems.image

import android.graphics.drawable.Drawable
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class ImageViewModel(
    override val id: String,
    val drawable: Drawable?
) : DrawerItemViewModel